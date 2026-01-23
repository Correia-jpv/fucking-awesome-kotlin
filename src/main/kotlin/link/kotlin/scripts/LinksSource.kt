package link.kotlin.scripts

import kotlinx.coroutines.runBlocking
import link.kotlin.scripts.dsl.Category
import link.kotlin.scripts.scripting.ScriptEvaluator
import java.nio.file.Files
import java.nio.file.Paths

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
