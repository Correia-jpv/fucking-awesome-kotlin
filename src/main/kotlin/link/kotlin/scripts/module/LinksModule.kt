package link.kotlin.scripts.module

import io.heapy.komok.tech.di.delegate.bean
import link.kotlin.scripts.CachedGithubTrending
import link.kotlin.scripts.CachingLinksProcessor
import link.kotlin.scripts.CategoryProcessor
import link.kotlin.scripts.CombinedLinksProcessors
import link.kotlin.scripts.DefaultLinksChecker
import link.kotlin.scripts.DefaultLinksProcessor
import link.kotlin.scripts.DescriptionMarkdownLinkProcessor
import link.kotlin.scripts.FileSystemLinksSource
import link.kotlin.scripts.GithubTrending
import link.kotlin.scripts.JSoupGithubTrending
import link.kotlin.scripts.LinksChecker
import link.kotlin.scripts.LinksProcessor
import link.kotlin.scripts.LinksSource
import link.kotlin.scripts.ParallelCategoryProcessor

class LinksModule(
    private val configurationModule: ConfigurationModule,
    private val utilsModule: UtilsModule,
    private val scriptingModule: ScriptingModule,
    private val renderingModule: RenderingModule,
) {
    val linksChecker by bean<LinksChecker> {
        DefaultLinksChecker(httpClient = utilsModule.httpClient.value)
    }

    val linksProcessor by bean<LinksProcessor> {
        val defaultLinksProcessor = DefaultLinksProcessor(
            configuration = configurationModule.applicationConfiguration.value,
            mapper = utilsModule.objectMapper.value,
            httpClient = utilsModule.httpClient.value,
            linksChecker = linksChecker.value
        )

        val cachedLinksProcessor = CachingLinksProcessor(
            cache = utilsModule.cache.value,
            delegate = defaultLinksProcessor
        )

        val markdownLinkProcessor = DescriptionMarkdownLinkProcessor(
            markdownRenderer = renderingModule.markdownRenderer.value
        )

        CombinedLinksProcessors(listOf(cachedLinksProcessor, markdownLinkProcessor))
    }

    val categoryProcessor by bean<CategoryProcessor> {
        ParallelCategoryProcessor(linksProcessor = linksProcessor.value)
    }

    val githubTrending by bean<GithubTrending> {
        val jSoupGithubTrending = JSoupGithubTrending()

        CachedGithubTrending(
            cache = utilsModule.cache.value,
            githubTrending = jSoupGithubTrending
        )
    }

    val linksSource by bean<LinksSource> {
        FileSystemLinksSource(
            scriptEvaluator = scriptingModule.scriptEvaluator.value,
            githubTrending = githubTrending.value,
            categoryProcessor = categoryProcessor.value
        )
    }
}
