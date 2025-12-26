import com.talangraga.data.BuildKonfig
import com.talangraga.data.network.model.response.DataResponse
import com.talangraga.data.network.model.response.TokenResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

/**
 * iqbalfauzi
 * Email: work.iqbalfauzi@gmail.com
 * Github: https://github.com/iqbalwork
 */
class FakeTokenManager {
    var accessToken: String? = null

    fun saveToken(token: String) {
        accessToken = token
    }
}

fun mockSuccessEngine(): MockEngine {
    return MockEngine { request ->
        respond(
            content = ByteReadChannel("""{"code":200,"message":"Access token refreshed successfully","data":{"access_token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ3b3JrLmlxYmFsZmF1emlAZ21haWwuY29tIiwiZXhwIjoxNzY2NTgzMjk5fQ.NqTridxUZ3_4vNdAhnBTfqMPcAzt5rNOg5cnpo9-WHI","token_type":"bearer"}}"""),
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
    }
}

class TestClient(engine: HttpClientEngine) {

    val httpClient = HttpClient(engine) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun getRefreshToken(refreshToken: String): DataResponse<TokenResponse> {
        return httpClient.post("${BuildKonfig.BASE_URL}auth/refresh") {
            header(HttpHeaders.Authorization, "Bearer $refreshToken")
        }.body()
    }
}

class RefreshTokenTest {

    private lateinit var mockSuccessEngine: MockEngine
    private lateinit var tokenManager: FakeTokenManager
    private lateinit var testClient: TestClient
    private lateinit var refreshToken: String

    @BeforeTest
    fun setUp() {
        refreshToken =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ3b3JrLmlxYmFsZmF1emlAZ21haWwuY29tIiwiZXhwIjoxNzY3MTg0NDQwfQ.C1E6eielzEQqBmnm8mka7gkpArZiUieZzRS5nBPia7o"
        mockSuccessEngine = mockSuccessEngine()
        tokenManager = FakeTokenManager()
        testClient = TestClient(mockSuccessEngine)
    }

    @Test
    fun `refresh token success saves new access token`() = runBlocking {
        val response = testClient.getRefreshToken(refreshToken)
        tokenManager.saveToken(response.data?.accessToken.orEmpty())
        val savedAccessToken = tokenManager.accessToken.orEmpty()
        assertEquals(
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ3b3JrLmlxYmFsZmF1emlAZ21haWwuY29tIiwiZXhwIjoxNzY2NTgzMjk5fQ.NqTridxUZ3_4vNdAhnBTfqMPcAzt5rNOg5cnpo9-WHI",
            response.data?.accessToken
        )
        assertEquals(response.data?.accessToken.orEmpty(), savedAccessToken)
    }

    @Test
    fun `refresh token not same with saved access token`() = runBlocking {
        val response = testClient.getRefreshToken(refreshToken)
        tokenManager.saveToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ3b3JrLmlxYmFsZmF1emlAZ21haWwuY29tIiwiZXhwIjoxNzY2NTgzMjk5fQ.NqTridxUZ3_4vNdAhnBTfqMPcAzt5rNOg5cnpo9-WHO")
        val savedAccessToken = tokenManager.accessToken.orEmpty()
        assertNotEquals(response.data?.accessToken.orEmpty(), savedAccessToken)
    }

    @AfterTest
    fun tearDown() {
        tokenManager.accessToken = null
    }
}
