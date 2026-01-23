package link.kotlin.scripts

import kotlinx.coroutines.runBlocking
import link.kotlin.scripts.dsl.Category
import link.kotlin.scripts.scripting.ScriptEvaluator
import link.kotlin.scripts.utils.Cache
import link.kotlin.scripts.utils.cacheKey
import link.kotlin.scripts.utils.logger
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

interface LinksSource {
    fun getLinks(): List<Category>
}

private val files = listOf(
    "Links.awesome.kts",
    "Libraries.awesome.kts",
    "Projects.awesome.kts",
    "Android.awesome.kts",
    "JavaScript.awesome.kts",
    "Native.awesome.kts",
    "WebAssembly.awesome.kts",
    "UserGroups.awesome.kts",
)

internal class FileSystemLinksSource(
    private val scriptEvaluator: ScriptEvaluator,
    private val githubTrending: GithubTrending,
    private val categoryProcessor: CategoryProcessor
) : LinksSource {
    override fun getLinks(): List<Category> = runBlocking {
        val scriptCategories = files.map { file ->
            val source = Files.readString(Paths.get("src/main/resources/links/", file))
            scriptEvaluator.eval(source, file, Category::class)
        }

        val trendingCategory = listOfNotNull(githubTrending.fetch())

        (trendingCategory + scriptCategories)
            .map { category -> categoryProcessor.process(category) }
    }
}

class CachingLinksSource(
    private val cache: Cache,
    private val delegate: LinksSource,
    private val maxAge: Duration = 12.hours
) : LinksSource {
    override fun getLinks(): List<Category> {
        val sourceFilesHash = computeSourceFilesHash()
        val cacheKey = "links-pipeline-${Cache.cacheKey(sourceFilesHash)}"

        val cached = cache.get(cacheKey, CachedCategories::class)
        if (cached != null && !cached.isExpired()) {
            LOGGER.info("Using cached links pipeline result")
            return cached.value
        }

        LOGGER.info("Cache miss - running full links pipeline")
        val result = delegate.getLinks()
        cache.put(cacheKey, CachedCategories(result, System.currentTimeMillis()))
        return result
    }

    private fun CachedCategories.isExpired(): Boolean {
        return System.currentTimeMillis() - createdAt > maxAge.inWholeMilliseconds
    }

    private fun computeSourceFilesHash(): String {
        val contents = files.joinToString("\n") { file ->
            val path = Paths.get("src/main/resources/links/", file)
            if (Files.exists(path)) Files.readString(path) else ""
        }
        return contents
    }

    companion object {
        private val LOGGER = logger<CachingLinksSource>()
    }
}

private data class CachedCategories(
    val value: List<Category>,
    val createdAt: Long
)
