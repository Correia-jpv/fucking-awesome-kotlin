package link.kotlin.scripts

import org.commonmark.parser.Parser
import org.commonmark.renderer.Renderer

interface MarkdownRenderer {
    fun render(md: String): String
}

internal class CommonMarkMarkdownRenderer(
    private val parser: Parser,
    private val renderer: Renderer
) : MarkdownRenderer {
    override fun render(md: String): String {
        val document = parser.parse(md)
        return renderer.render(document)
    }
}
