import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import io.heapy.komok.tech.config.dotenv.dotenv
import io.heapy.komok.tech.di.delegate.bean

class ConfigModule {
    val overrides by bean {
        mapOf<String, String>()
    }

    val dotenv by bean {
        dotenv().properties
    }

    val config by bean<Config> {
        (dotenv.value + overrides.value).forEach {
            System.setProperty(it.key, it.value)
        }

        ConfigFactory.load()
    }
}
