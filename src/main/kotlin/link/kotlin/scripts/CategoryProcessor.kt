package link.kotlin.scripts

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import link.kotlin.scripts.dsl.Category

interface CategoryProcessor {
    suspend fun process(category: Category): Category
}

internal class ParallelCategoryProcessor(
    private val linksProcessor: LinksProcessor
) : CategoryProcessor {
    override suspend fun process(category: Category): Category = coroutineScope {
        val result = category.copy(
            subcategories = category.subcategories.map { subcategory ->
                subcategory.copy(
                    links = subcategory.links.map { link ->
                        async { linksProcessor.process(link) }
                    }.awaitAll().toMutableList()
                )
            }.toMutableList()
        )

        result
    }
}
