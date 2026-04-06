package usecases.github

import HttpClientModule
import infra.config.ConfigModule
import infra.config.decode
import io.heapy.komok.tech.di.delegate.bean

class GithubModule(
    private val configModule: ConfigModule,
    private val httpClientModule: HttpClientModule,
) {
    val githubAuthConfig by bean<GithubAuthConfig> {
        configModule.decode("github")
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
