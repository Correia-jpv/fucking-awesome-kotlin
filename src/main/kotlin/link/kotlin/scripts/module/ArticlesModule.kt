package link.kotlin.scripts.module

import io.heapy.komok.tech.di.delegate.bean
import link.kotlin.scripts.ArticlesProcessor
import link.kotlin.scripts.ArticlesSource
import link.kotlin.scripts.DefaultArticlesProcessor
import link.kotlin.scripts.FileSystemArticlesSource

class ArticlesModule(
    private val scriptingModule: ScriptingModule,
    private val renderingModule: RenderingModule,
    private val utilsModule: UtilsModule,
) {
    val articlesProcessor by bean<ArticlesProcessor> {
        DefaultArticlesProcessor(markdownRenderer = renderingModule.markdownRenderer.value)
    }

    val articlesSource by bean<ArticlesSource> {
        FileSystemArticlesSource(
            scriptEvaluator = scriptingModule.scriptEvaluator.value,
            articlesProcessor = articlesProcessor.value,
            jsonMapper = utilsModule.objectMapper.value,
        )
    }
}
