package usecases.kug

import HttpClientModule
import YamlModule
import io.heapy.komok.tech.di.delegate.buildModule
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.AssertionFailureBuilder.assertionFailure
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class KugDownloadServiceTest {
    private val httpClientModule = buildModule<HttpClientModule>()
    private val yamlModule = buildModule<YamlModule>()

    private val kugDownloadService = KugDownloadService(
        yaml = yamlModule.yaml.value,
        httpClient = httpClientModule.httpClient.value,
    )

    @AfterEach
    fun close() {
        httpClientModule.close()
    }

    @Test
    fun `test getting yaml file with user groups`() = runTest {
        val yaml = kugDownloadService.download()

        if (!yaml.contains("https://bkug.by/")) {
            assertionFailure()
                .message("yaml")
                .expected("Yaml with Kotlin User Groups")
                .actual(yaml)
                .buildAndThrow()
        }
    }

    @Test
    fun `test parsing yaml file with user groups`() = runTest {
        val sections = kugDownloadService.pull()
        val bkug = sections.find { it.section == "Europe" }
            ?.groups
            ?.find { it.url == "https://bkug.by/" }

        assertEquals(
            KugDownloadService.Kug(
                name = "Belarus KUG",
                country = "Belarus",
                url = "https://bkug.by/",
                position = KugDownloadService.Position(
                    lat = 53.709807,
                    lng = 27.953389,
                ),
            ),
            bkug
        )
    }
}
