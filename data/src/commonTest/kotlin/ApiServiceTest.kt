import com.talangraga.data.network.api.ApiService
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.toByteArray
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ApiServiceTest {

    @Test
    fun `getEmailByIdentifier success returns resolved email`() = runBlocking {
        val mockEngine = MockEngine { request ->
            when (request.url.encodedPath) {
                "/rest/v1/rpc/get_email_by_identifier" -> {
                    respond(
                        content = ByteReadChannel("\"work.iqbalfauzi@gmail.com\""),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
                else -> respond("Not found", HttpStatusCode.NotFound)
            }
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val apiService = ApiService(client)
        val email = apiService.getEmailByIdentifier("adminIdentifier")
        assertEquals("work.iqbalfauzi@gmail.com", email)
    }

    @Test
    fun `getEmailByIdentifier non-success throws exception`() = runBlocking {
        val mockEngine = MockEngine { _ ->
            respond(
                content = ByteReadChannel("{\"msg\":\"Identifier not found\"}"),
                status = HttpStatusCode.BadRequest,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val apiService = ApiService(client)
        val exception = assertFailsWith<Exception> {
            apiService.getEmailByIdentifier("invalid_user")
        }
        assertEquals("Identifier not found", exception.message)
    }

    @Test
    fun `login flow resolves email before posting to token endpoint`() = runBlocking {
        var emailUsedInTokenRequest: String? = null

        val mockEngine = MockEngine { request ->
            when {
                request.url.encodedPath.contains("get_email_by_identifier") -> {
                    respond(
                        content = ByteReadChannel("\"resolved.user@gmail.com\""),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
                request.url.encodedPath.contains("auth/v1/token") -> {
                    val bodyString = request.body.toByteArray().decodeToString()
                    if (bodyString.contains("resolved.user@gmail.com")) {
                        emailUsedInTokenRequest = "resolved.user@gmail.com"
                    }
                    respond(
                        content = ByteReadChannel("{\"access_token\":\"mock_access_token\",\"refresh_token\":\"mock_refresh_token\"}"),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
                else -> respond("Not found", HttpStatusCode.NotFound)
            }
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val apiService = ApiService(client)
        val response = apiService.login("username123", "password123")

        assertEquals("mock_access_token", response.accessToken)
        assertEquals("resolved.user@gmail.com", emailUsedInTokenRequest)
    }

    @Test
    fun `registerUser without imageProfile calls signup and returns response`() = runBlocking {
        val mockEngine = MockEngine { request ->
            when {
                request.url.encodedPath.contains("auth/v1/signup") -> {
                    respond(
                        content = ByteReadChannel(
                            """
                            {
                                "access_token": "token_123",
                                "refresh_token": "refresh_123",
                                "user": {
                                    "id": "usr-123",
                                    "email": "test@talangraga.com",
                                    "user_metadata": {
                                        "fullname": "Test User",
                                        "username": "testuser"
                                    }
                                }
                            }
                            """.trimIndent()
                        ),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
                else -> respond("Not found", HttpStatusCode.NotFound)
            }
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val apiService = ApiService(client)
        val result = apiService.registerUser(
            fullname = "Test User",
            username = "testuser",
            email = "test@talangraga.com",
            phone = "0812345678",
            password = "Password123!",
            domicile = "Jakarta",
            userType = "member",
            imageProfile = null
        )

        assertEquals("token_123", result.accessToken)
        assertEquals("usr-123", result.userResponse?.id)
        assertEquals("Test User", result.userResponse?.fullname)
    }

    @Test
    fun `registerUser with imageProfile uploads avatar to storage and patches profile`() = runBlocking {
        var uploadedStoragePath: String? = null
        var patchedProfileBody: String? = null

        val mockEngine = MockEngine { request ->
            when {
                request.url.encodedPath.contains("auth/v1/signup") -> {
                    respond(
                        content = ByteReadChannel(
                            """
                            {
                                "access_token": "token_abc",
                                "refresh_token": "refresh_abc",
                                "user": {
                                    "id": "usr-777",
                                    "email": "avatar@talangraga.com",
                                    "user_metadata": {
                                        "fullname": "Avatar User",
                                        "username": "avataruser"
                                    }
                                }
                            }
                            """.trimIndent()
                        ),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
                request.url.encodedPath.contains("storage/v1/object/avatars/usr-777") -> {
                    uploadedStoragePath = request.url.encodedPath
                    respond(
                        content = ByteReadChannel("{\"Key\":\"avatars/usr-777/avatar_sample.jpg\"}"),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
                request.url.encodedPath.contains("rest/v1/profiles") -> {
                    patchedProfileBody = request.body.toByteArray().decodeToString()
                    respond(
                        content = ByteReadChannel(
                            """
                            [
                                {
                                    "id": "usr-777",
                                    "fullname": "Avatar User",
                                    "username": "avataruser",
                                    "image_profile_url": "https://gcxaktjbwiduirhcxgrv.supabase.co/storage/v1/object/public/avatars/usr-777/avatar_sample.jpg"
                                }
                            ]
                            """.trimIndent()
                        ),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
                else -> respond("Not found", HttpStatusCode.NotFound)
            }
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val apiService = ApiService(client)
        val dummyBytes = byteArrayOf(1, 2, 3, 4, 5)
        val result = apiService.registerUser(
            fullname = "Avatar User",
            username = "avataruser",
            email = "avatar@talangraga.com",
            phone = "0811111111",
            password = "Password123!",
            domicile = "Bandung",
            userType = "member",
            imageProfile = dummyBytes
        )

        assertEquals("token_abc", result.accessToken)
        assertEquals("usr-777", result.userResponse?.id)
        assert(uploadedStoragePath?.contains("storage/v1/object/avatars/usr-777") == true)
        assert(patchedProfileBody?.contains("image_profile_url") == true)
        assert(result.userResponse?.rawImageProfileUrl?.contains("storage/v1/object/public/avatars/usr-777") == true)
    }
}
