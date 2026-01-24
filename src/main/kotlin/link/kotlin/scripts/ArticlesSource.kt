package link.kotlin.scripts

import link.kotlin.scripts.dsl.Article
import link.kotlin.scripts.scripting.ScriptEvaluator
import tools.jackson.databind.json.JsonMapper
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.io.path.writeText

interface ArticlesSource {
    fun getArticles(): List<Article>
}

internal class FileSystemArticlesSource(
    private val scriptEvaluator: ScriptEvaluator,
    private val articlesProcessor: ArticlesProcessor,
    private val jsonMapper: JsonMapper,
) : ArticlesSource {
    override fun getArticles(): List<Article> {
        return Files.list(Paths.get("articles"))
            .toList()
            .filter { it.fileName.toString().endsWith(".awesome.kts") }
            .map { path ->
                val source = Files.readString(path)
                scriptEvaluator.eval(source, path.toString(), Article::class)
            }
            .sortedWith { a, b -> b.date.compareTo(a.date) }
            .also { articles ->
                Path("articles.json").writeText(jsonMapper.writeValueAsString(articles))
            }
            .map(articlesProcessor::process)
    }
}

