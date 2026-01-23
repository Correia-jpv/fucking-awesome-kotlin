package usecases.version

import HttpClientModule
import XmlModule
import io.heapy.komok.tech.di.delegate.bean

class KotlinVersionModule(
    private val xmlModule: XmlModule,
    private val httpClientModule: HttpClientModule,
) {
    val versionFetcher by bean<KotlinVersionFetcher> {
        MavenCentralKotlinVersionFetcher(
            xmlMapper = xmlModule.xmlMapper.value,
            httpClient = httpClientModule.httpClient.value,
        )
    }
}
