package link.kotlin.scripts.module

import io.heapy.komok.tech.di.delegate.bean
import link.kotlin.scripts.utils.Cache
import link.kotlin.scripts.utils.DefaultHttpClient
import link.kotlin.scripts.utils.DisableCache
import link.kotlin.scripts.utils.FileCache
import link.kotlin.scripts.utils.HttpClient
import link.kotlin.scripts.utils.KotlinObjectMapper
import link.kotlin.scripts.utils.logger
import org.apache.http.impl.cookie.IgnoreSpecProvider
import org.apache.http.impl.nio.client.HttpAsyncClients
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.concurrent.thread
import kotlin.time.Duration.Companion.hours

class UtilsModule(
    private val configurationModule: ConfigurationModule,
) {
    val objectMapper by bean {
        KotlinObjectMapper()
    }

    val httpClient by bean<HttpClient> {
        val ua = "Mozilla/5.0 (X11; Fedora; Linux x86_64; rv:74.0) Gecko/20100101 Firefox/74.0"

        val asyncClient = HttpAsyncClients.custom()
            .setUserAgent(ua)
            .setMaxConnPerRoute(10)
            .setMaxConnTotal(100)
            .setDefaultCookieSpecRegistry { IgnoreSpecProvider() }
            .build()

        Runtime.getRuntime().addShutdownHook(thread(start = false) {
            logger<HttpClient>().info("HttpClient Shutdown called...")
            asyncClient.close()
            logger<HttpClient>().info("HttpClient Shutdown done...")
        })

        asyncClient.start()

        DefaultHttpClient(client = asyncClient)
    }

    val cache by bean<Cache> {
        val folder = Paths.get(System.getProperty("user.home"), ".cache", "awesome-kotlin")
        Files.createDirectories(folder)

        val fileCache = FileCache(
            folder = folder,
            mapper = objectMapper.value
        )

        fileCache.cleanup(prefix = "link-", maxAge = 12.hours)

        DisableCache(
            cache = fileCache,
            configuration = configurationModule.applicationConfiguration.value
        )
    }
}
