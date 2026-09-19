package com.talangraga.data.network.api

import com.talangraga.data.AppConfig
import com.talangraga.data.network.model.response.PaymentResponse
import com.talangraga.data.network.model.response.PeriodeResponse
import com.talangraga.data.network.model.response.TokenResponse
import com.talangraga.data.network.model.response.TransactionResponse
import com.talangraga.data.network.model.response.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.github.aakira.napier.Napier
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.delay
import kotlin.time.Clock
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject

class ApiService(private val httpClient: HttpClient) {

    private val authProvider = httpClient.authProvider<BearerAuthProvider>()

    suspend fun getEmailByIdentifier(identifier: String): String {
        val response = httpClient.post("rest/v1/rpc/get_email_by_identifier") {
            contentType(ContentType.Application.Json)
            setBody(buildJsonObject {
                put("p_identifier", identifier)
            })
        }
        if (!response.status.isSuccess()) {
            val errorMsg = try {
                val json = response.body<JsonObject>()
                json["msg"]?.jsonPrimitive?.contentOrNull
                    ?: json["message"]?.jsonPrimitive?.contentOrNull
                    ?: json["error_description"]?.jsonPrimitive?.contentOrNull
                    ?: json["details"]?.jsonPrimitive?.contentOrNull
            } catch (_: Exception) {
                null
            } ?: "Identifier not found"
            throw Exception(errorMsg)
        }
        val rawEmail = response.body<String>().trim().removeSurrounding("\"")
        if (rawEmail.isBlank() || rawEmail == "null") {
            throw Exception("Identifier not found")
        }
        return rawEmail
    }

    suspend fun login(identifier: String, password: String): TokenResponse {
        authProvider?.clearToken()
        val email = getEmailByIdentifier(identifier)
        val response = httpClient.post("auth/v1/token?grant_type=password") {
            contentType(ContentType.Application.Json)
            setBody(buildJsonObject {
                put("email", email)
                put("password", password)
            })
        }
        if (!response.status.isSuccess()) {
            val errorMsg = try {
                val json = response.body<JsonObject>()
                json["msg"]?.jsonPrimitive?.contentOrNull
                    ?: json["message"]?.jsonPrimitive?.contentOrNull
                    ?: json["error_description"]?.jsonPrimitive?.contentOrNull
            } catch (_: Exception) {
                null
            } ?: "Invalid login credentials"
            throw Exception(errorMsg)
        }
        val tokenResponse = response.body<TokenResponse>()
        if (tokenResponse.accessToken.isNullOrBlank()) {
            throw Exception("Invalid login credentials")
        }
        return tokenResponse
    }

    suspend fun getCurrentUser(): UserResponse? {
        return try {
            val response = httpClient.get("auth/v1/user")
            if (response.status.isSuccess()) {
                response.body<UserResponse>()
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    suspend fun getLoginProfile(currentUserId: String): UserResponse {
        val targetId = if (currentUserId.isNotBlank()) {
            currentUserId
        } else {
            getCurrentUser()?.id.orEmpty()
        }
        val response = httpClient.get("rest/v1/profiles") {
            url {
                if (targetId.isNotBlank()) {
                    parameters.append("id", "eq.$targetId")
                }
                parameters.append("select", "*")
            }
        }
        if (!response.status.isSuccess()) {
            throw Exception("Failed to load profile: ${response.status}")
        }
        val list = response.body<List<UserResponse>>()
        return list.firstOrNull() ?: throw Exception("Profile not found")
    }

    suspend fun logout() {
        try {
            httpClient.post("auth/v1/logout")
        } catch (_: Exception) {
            // Best effort remote logout
        } finally {
            authProvider?.clearToken()
        }
    }

    @OptIn(kotlin.time.ExperimentalTime::class)
    suspend fun uploadAvatar(
        userId: String,
        imageBytes: ByteArray,
        userToken: String? = null
    ): String {
        val isPng = imageBytes.size > 8 &&
                imageBytes[0] == 0x89.toByte() &&
                imageBytes[1] == 0x50.toByte() &&
                imageBytes[2] == 0x4E.toByte() &&
                imageBytes[3] == 0x47.toByte()
        val contentType = if (isPng) ContentType.Image.PNG else ContentType.Image.JPEG
        val fileName = "${userId}_avatar.jpg"
        val storagePath = "storage/v1/object/avatars/$userId/$fileName"

        val response = httpClient.post(storagePath) {
            header("x-upsert", "true")
            contentType(contentType)
            if (!userToken.isNullOrBlank()) {
                header("Authorization", "Bearer $userToken")
            }
            setBody(imageBytes)
        }

        if (!response.status.isSuccess()) {
            val errorBody = try { response.bodyAsText() } catch (_: Exception) { "" }
            Napier.e("Failed to upload avatar to Supabase Storage: ${response.status} - $errorBody")
            throw Exception("Gagal upload avatar ke storage: ${response.status.value} $errorBody")
        }

        val publicUrl = "${AppConfig.BASE_URL.trimEnd('/')}/storage/v1/object/public/avatars/$userId/$fileName"
        Napier.i("Avatar uploaded successfully to Supabase: $publicUrl")
        return publicUrl
    }

    suspend fun updateProfileAvatar(
        userId: String,
        avatarUrl: String,
        userToken: String? = null
    ) {
        var attempts = 0
        while (attempts < 3) {
            attempts++
            try {
                val patchResponse = httpClient.patch("rest/v1/profiles") {
                    header("Prefer", "return=representation")
                    url {
                        parameters.append("id", "eq.$userId")
                    }
                    if (!userToken.isNullOrBlank()) {
                        header("Authorization", "Bearer $userToken")
                    }
                    contentType(ContentType.Application.Json)
                    setBody(buildJsonObject {
                        put("image_profile_url", avatarUrl)
                    })
                }
                if (patchResponse.status.isSuccess()) {
                    val updated = patchResponse.body<List<UserResponse>>()
                    if (updated.isNotEmpty()) {
                        Napier.i("Successfully updated profile avatar in Supabase: $avatarUrl")
                        return
                    }
                }
            } catch (e: Exception) {
                Napier.w("Attempt $attempts to update profile avatar failed", e)
            }
            if (attempts < 3) {
                delay(300)
            }
        }
    }

    suspend fun registerUser(
        fullname: String,
        username: String,
        email: String,
        phone: String?,
        password: String,
        domicile: String?,
        userType: String,
        imageProfile: ByteArray? = null
    ): TokenResponse {
        val response = httpClient.post("auth/v1/signup") {
            contentType(ContentType.Application.Json)
            setBody(buildJsonObject {
                put("email", email)
                put("password", password)
                putJsonObject("data") {
                    put("fullname", fullname)
                    put("username", username)
                    phone?.let { put("phone_number", it) }
                    domicile?.let { put("domisili", it) }
                    put("user_type", userType)
                }
            })
        }
        if (!response.status.isSuccess()) {
            val errorMsg = try {
                val json = response.body<JsonObject>()
                json["msg"]?.jsonPrimitive?.contentOrNull
                    ?: json["message"]?.jsonPrimitive?.contentOrNull
                    ?: json["error_description"]?.jsonPrimitive?.contentOrNull
            } catch (_: Exception) {
                null
            } ?: "Registration failed (${response.status.value})"
            throw Exception(errorMsg)
        }

        val tokenResponse = response.body<TokenResponse>()
        val newUserId = tokenResponse.userResponse?.id?.ifBlank { null }
            ?: tokenResponse.id?.ifBlank { null }

        var avatarUrl: String? = null
        if (imageProfile != null && !newUserId.isNullOrBlank()) {
            val userToken = tokenResponse.accessToken?.ifBlank { null }
            try {
                avatarUrl = uploadAvatar(newUserId, imageProfile, userToken)
                updateProfileAvatar(newUserId, avatarUrl, userToken)
            } catch (e: Exception) {
                Napier.e("Gagal upload avatar profil saat registrasi", e)
            }
        }

        val finalUser = (tokenResponse.userResponse ?: UserResponse(id = newUserId.orEmpty())).copy(
            rawImageProfileUrl = avatarUrl ?: tokenResponse.userResponse?.rawImageProfileUrl,
            rawFullname = tokenResponse.userResponse?.fullname ?: fullname,
            rawUsername = tokenResponse.userResponse?.username ?: username,
            rawEmail = tokenResponse.userResponse?.email ?: email,
            rawPhone = tokenResponse.userResponse?.phone ?: phone,
            rawDomisili = tokenResponse.userResponse?.domisili ?: domicile,
            rawUserType = tokenResponse.userResponse?.userType ?: userType
        )

        return tokenResponse.copy(userResponse = finalUser)
    }

    suspend fun getListUsers(): List<UserResponse> {
        return httpClient.get("rest/v1/profiles") {
            url {
                parameters.append("select", "*")
                parameters.append("order", "created_at.desc")
            }
        }.body()
    }

    suspend fun getPeriods(): List<PeriodeResponse> {
        return httpClient.get("rest/v1/periodes") {
            url {
                parameters.append("select", "*")
                parameters.append("order", "id.asc")
            }
        }.body()
    }

    suspend fun addPeriode(
        periodeName: String,
        startDate: String,
        endDate: String
    ): PeriodeResponse {
        val response = httpClient.post("rest/v1/periodes") {
            header("Prefer", "return=representation")
            contentType(ContentType.Application.Json)
            setBody(buildJsonObject {
                put("periode_name", periodeName)
                put("start_date", startDate)
                put("end_date", endDate)
            })
        }.body<List<PeriodeResponse>>()
        return response.first()
    }

    suspend fun getPayments(): List<PaymentResponse> {
        return httpClient.get("rest/v1/payments") {
            url {
                parameters.append("select", "*")
                parameters.append("order", "id.asc")
            }
        }.body()
    }

    suspend fun getTransactions(
        periodId: Int?,
        status: String?,
        paymentId: Int?
    ): List<TransactionResponse> {
        return httpClient.get("rest/v1/transactions") {
            url {
                parameters.append("select", "*,periode:periodes(*),payment:payments(*),user:profiles!user_id(*)")
                parameters.append("order", "transaction_date.desc")
                periodId?.let { parameters.append("periode_id", "eq.$it") }
                status?.let { parameters.append("status", "eq.$it") }
                paymentId?.let { parameters.append("payment_id", "eq.$it") }
            }
        }.body()
    }

    suspend fun updateUser(
        userId: String,
        fullname: String,
        phone: String?,
        domicile: String?,
        username: String? = null,
        userType: String? = null,
        imageProfile: ByteArray? = null
    ): UserResponse {
        var imageUrl: String? = null
        if (imageProfile != null) {
            try {
                imageUrl = uploadAvatar(userId, imageProfile)
            } catch (e: Exception) {
                Napier.e("Failed to upload avatar in updateUser", e)
            }
        }

        val list = httpClient.patch("rest/v1/profiles") {
            header("Prefer", "return=representation")
            url {
                parameters.append("id", "eq.$userId")
            }
            contentType(ContentType.Application.Json)
            setBody(buildJsonObject {
                put("fullname", fullname)
                phone?.let { put("phone_number", it) }
                domicile?.let { put("domisili", it) }
                username?.let { if (it.isNotBlank()) put("username", it) }
                userType?.let { if (it.isNotBlank()) put("user_type", it) }
                imageUrl?.let {
                    put("image_profile_url", it)
                }
            })
        }.body<List<UserResponse>>()
        return list.firstOrNull() ?: throw Exception("Failed to update profile")
    }

    suspend fun updateMe(
        userId: String,
        fullname: String,
        phone: String?,
        domicile: String?,
        imageProfile: ByteArray? = null
    ): UserResponse {
        return updateUser(
            userId = userId,
            fullname = fullname,
            phone = phone,
            domicile = domicile,
            imageProfile = imageProfile
        )
    }

    suspend fun changePassword(
        newPassword: String
    ) {
        val response = httpClient.post("auth/v1/user") {
            contentType(ContentType.Application.Json)
            setBody(buildJsonObject {
                put("password", newPassword)
            })
        }
        if (!response.status.isSuccess()) {
            val errorMsg = try {
                val json = response.body<JsonObject>()
                json["msg"]?.jsonPrimitive?.contentOrNull
                    ?: json["message"]?.jsonPrimitive?.contentOrNull
            } catch (_: Exception) {
                null
            } ?: "Failed to change password (${response.status.value})"
            throw Exception(errorMsg)
        }
    }

    suspend fun addTransaction(
        userId: String?,
        reportedByUserId: String?,
        amount: Double?,
        transactionDate: String?,
        periodeId: Int?,
        paymentId: Int?,
        file: ByteArray?
    ): TransactionResponse {
        var buktiTransferUrl = ""

        if (file != null) {
            val safeDate = transactionDate?.replace(":", "")?.replace(" ", "_")?.replace("-", "") ?: "date"
            val targetUserId = userId ?: "anonymous"
            val fileName = "${targetUserId}_${safeDate}_transaction.jpg"
            val storagePath = "storage/v1/object/transfer-proofs/$targetUserId/$fileName"

            httpClient.post(storagePath) {
                header("x-upsert", "true")
                contentType(ContentType.Image.JPEG)
                setBody(file)
            }

            buktiTransferUrl = "${AppConfig.BASE_URL}storage/v1/object/public/transfer-proofs/$targetUserId/$fileName"
        }

        val list = httpClient.post("rest/v1/transactions") {
            header("Prefer", "return=representation")
            url {
                parameters.append("select", "*,periode:periodes(*),payment:payments(*)")
            }
            contentType(ContentType.Application.Json)
            setBody(buildJsonObject {
                amount?.let { put("amount", it) }
                transactionDate?.let { put("transaction_date", it) }
                put("status", "sent")
                userId?.let { put("user_id", it) }
                reportedByUserId?.let { put("reported_by_id", it) }
                periodeId?.let { put("periode_id", it) }
                paymentId?.let { put("payment_id", it) }
                if (buktiTransferUrl.isNotEmpty()) {
                    put("bukti_transfer_url", buktiTransferUrl)
                }
            })
        }.body<List<TransactionResponse>>()

        return list.first()
    }

    suspend fun updateTransactionStatus(
        transactionId: Int,
        status: String,
        confirmedById: String? = null
    ): TransactionResponse {
        val list = httpClient.patch("rest/v1/transactions") {
            header("Prefer", "return=representation")
            url {
                parameters.append("id", "eq.$transactionId")
                parameters.append("select", "*,periode:periodes(*),payment:payments(*),user:profiles!user_id(*)")
            }
            contentType(ContentType.Application.Json)
            setBody(buildJsonObject {
                put("status", status)
                confirmedById?.let { put("confirmed_by_id", it) }
            })
        }.body<List<TransactionResponse>>()
        return list.first()
    }

    suspend fun deleteTransaction(
        transactionId: Int
    ) {
        httpClient.delete("rest/v1/transactions") {
            url {
                parameters.append("id", "eq.$transactionId")
            }
        }
    }
}
