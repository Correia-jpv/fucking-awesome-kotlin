package usecases.github

import ConfigModule
import HttpClientModule
import io.heapy.komok.tech.di.delegate.bean
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.hocon.decodeFromConfig

class GithubModule(
    private val configModule: ConfigModule,
    private val httpClientModule: HttpClientModule,
) {
    val githubAuthConfig by bean<GithubAuthConfig> {
        Hocon.decodeFromConfig(configModule.config.value.getConfig("github"))
    }

    val githubRedirectUrl by bean {
        GithubRedirectUrl(
            githubAuthConfig = githubAuthConfig.value,
        )
    }

    val githubRedirectRoute by bean {
        GithubRedirectRoute(
            githubRedirectUrl = githubRedirectUrl.value,
        )
    }

    val githubAccessToken by bean {
        GithubAccessToken(
            githubAuthConfig = githubAuthConfig.value,
            httpClient = httpClientModule.httpClient.value,
        )
    }

    val githubCallbackRoute by bean {
        GithubCallbackRoute(
            githubAccessToken = githubAccessToken.value,
        )
    }
}
