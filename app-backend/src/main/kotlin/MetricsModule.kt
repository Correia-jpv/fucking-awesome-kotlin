import io.heapy.komok.tech.di.delegate.bean
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry

class MetricsModule {
    val meterRegistry by bean {
        PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
    }
}