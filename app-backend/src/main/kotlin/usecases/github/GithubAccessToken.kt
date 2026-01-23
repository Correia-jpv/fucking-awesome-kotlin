package usecases.github

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.server.util.url
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class GithubAccessToken(
    private val githubAuthConfig: GithubAuthConfig,
    private val httpClient: HttpClient,
) {
    suspend operator fun invoke(code: String): GithubAccessTokenResponse {
        return httpClient.post(accessTokenUrl(code))
            .body()
    }

    @Serializable
    data class GithubAccessTokenResponse(
        @SerialName("access_token")
        val accessToken: String,
        @SerialName("expires_in")
        val expiresIn: Int,
        @SerialName("refresh_token")
        val refreshToken: String,
        @SerialName("refresh_token_expires_in")
        val refreshTokenExpiresIn: Int,
        @SerialName("token_type")
        val tokenType: String,
        val scope: String,
    )

    internal fun accessTokenUrl(
        code: String,
    ): String {
        return url {
            protocol = URLProtocol.HTTPS
            host = "github.com"
            path("login", "oauth", "access_token")

            parameters.append("client_id", githubAuthConfig.clientId)
            parameters.append("client_secret", githubAuthConfig.clientSecret)
            parameters.append("code", code)
            parameters.append("redirect_uri", githubAuthConfig.redirectUri)
        }
    }
}
