package link.kotlin.scripts.module

import io.heapy.komok.tech.di.delegate.bean
import link.kotlin.scripts.CommonMarkMarkdownRenderer
import link.kotlin.scripts.MarkdownRenderer
import org.commonmark.ext.gfm.tables.TablesExtension
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer

class RenderingModule {
    val markdownRenderer by bean<MarkdownRenderer> {
        val extensions = listOf(TablesExtension.create())
        val parser = Parser.builder().extensions(extensions).build()
        val renderer = HtmlRenderer.builder().extensions(extensions).build()

        CommonMarkMarkdownRenderer(
            parser = parser,
            renderer = renderer
        )
    }
}
