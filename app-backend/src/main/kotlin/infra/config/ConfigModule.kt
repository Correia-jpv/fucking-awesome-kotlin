package infra.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import io.heapy.komok.tech.config.dotenv.dotenv
import io.heapy.komok.tech.di.lib.Module
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.hocon.decodeFromConfig

@Module
open class ConfigModule {
    open val overrides by lazy {
        mapOf<String, String>()
    }

    open val dotenv by lazy {
        dotenv()
    }

    open val env by lazy {
        buildMap {
            putAll(System.getenv())
            putAll(dotenv.properties)
            putAll(overrides)
        }
    }

    open val config: Config by lazy {
        env.forEach {
            System.setProperty(it.key, it.value)
        }

        ConfigFactory.load()
    }
}

@OptIn(ExperimentalSerializationApi::class)
inline fun <reified T> ConfigModule.decode(
    path: String,
): T {
    return Hocon.decodeFromConfig(config.getConfig(path))
}
