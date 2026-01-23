package link.kotlin.scripts.model

import java.lang.System.getenv

interface ApplicationConfiguration {
    val ghToken: String
    val siteUrl: String
    val cacheEnabled: Boolean
    val dryRun: Boolean
}

data class DataApplicationConfiguration(
    override val ghToken: String = env("GH_TOKEN", ""),
    override val siteUrl: String = "https://kotlin.link/",
    override val cacheEnabled: Boolean = env("AWESOME_KOTLIN_CACHE", "true").toBoolean(),
    override val dryRun: Boolean = false
): ApplicationConfiguration

private fun env(key: String, default: String): String {
    return getenv(key) ?: default
}
