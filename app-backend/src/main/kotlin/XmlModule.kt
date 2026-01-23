import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import io.heapy.komok.tech.di.delegate.bean

class XmlModule {
    val xmlMapper by bean {
        XmlMapper().apply {
            registerModule(kotlinModule { })
        }
    }
}
