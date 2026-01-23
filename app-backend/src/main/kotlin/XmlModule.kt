import tools.jackson.dataformat.xml.XmlMapper
import tools.jackson.module.kotlin.kotlinModule
import io.heapy.komok.tech.di.delegate.bean

class XmlModule {
    val xmlMapper by bean {
        XmlMapper.builder()
            .addModule(kotlinModule { })
            .build()
    }
}
