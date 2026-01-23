import io.heapy.komok.tech.di.delegate.bean
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import utils.close

class HttpClientModule : AutoCloseable {
    val httpClient by bean {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(json = Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    override fun close() {
        if (httpClient.isInitialized) httpClient.value.close {}
    }
}
