package link.kotlin.scripts.utils

import link.kotlin.scripts.model.ApplicationConfiguration
import tools.jackson.databind.ObjectMapper
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest
import kotlin.reflect.KClass
import kotlin.time.Duration

interface Cache {
    fun <T> put(key: String, value: T)
    fun <T : Any> get(key: String, type: KClass<T>): T?

    companion object
}

fun Cache.Companion.cacheKey(data: String): String {
    val magnitude = MessageDigest.getInstance("MD5").digest(data.toByteArray())
    return "%032x".format(BigInteger(1, magnitude))
}

class FileCache(
    private val folder: Path,
    private val mapper: ObjectMapper
) : Cache {
    override fun <T> put(key: String, value: T) {
        val data = mapper.writeValueAsString(value)
        writeFile(folder.resolve(key), data)
    }

    override fun <T : Any> get(key: String, type: KClass<T>): T? {
        val path = folder.resolve(key)
        return if (Files.exists(path)) {
            try {
                val data = Files.readAllBytes(path)
                mapper.readValue(data, type.java)
            } catch (e: Exception) {
                LOGGER.info("Removing invalid cache entry [$path].")
                Files.delete(path)
                null
            }
        } else null
    }

    fun cleanup(prefix: String, maxAge: Duration) {
        if (!Files.exists(folder)) return

        val cutoff = System.currentTimeMillis() - maxAge.inWholeMilliseconds
        Files.list(folder).use { stream ->
            stream
                .filter { it.fileName.toString().startsWith(prefix) }
                .filter { Files.getLastModifiedTime(it).toMillis() < cutoff }
                .forEach { path ->
                    LOGGER.info("Removing stale cache entry [$path].")
                    Files.delete(path)
                }
        }
    }

    companion object {
        private val LOGGER = logger<FileCache>()
    }
}

internal class DisableCache(
    private val cache: Cache,
    private val configuration: ApplicationConfiguration
) : Cache {
    override fun <T> put(key: String, value: T) {
        if (configuration.cacheEnabled) {
            cache.put(key, value)
        }
    }

    override fun <T : Any> get(key: String, type: KClass<T>): T? {
        return if (configuration.cacheEnabled) {
            cache.get(key, type)
        } else null
    }

}
