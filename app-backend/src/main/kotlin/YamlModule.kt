import com.charleskorn.kaml.Yaml
import io.heapy.komok.tech.di.delegate.bean

class YamlModule {
    val yaml by bean {
        Yaml.default
    }
}