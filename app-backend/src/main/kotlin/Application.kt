@file:JvmName("Application")

import io.heapy.komok.tech.di.delegate.bean
import io.heapy.komok.tech.di.delegate.buildModule
import utils.close
import utils.logger
import java.lang.management.ManagementFactory
import java.lang.management.RuntimeMXBean
import kotlin.time.Duration.Companion.milliseconds

suspend fun main() {
    buildModule<ApplicationModule>().use {
        it.run()
    }
}

class ApplicationModule(
    private val httpClientModule: HttpClientModule,
    private val jdbcModule: JdbcModule,
    private val lifecycleModule: LifecycleModule,
    private val flywayModule: FlywayModule,
    private val serverModule: ServerModule,
) : AutoCloseable {

    suspend fun run() {
        val gracefulShutdown = lifecycleModule.gracefulShutdown.value
        lifecycleModule.shutdownHandler.value.registerHook()
        flywayModule.flyway.value.migrate()
        serverModule.ktorServer.value.start(wait = false)
        log.value.info("Server started in {}", runtimeMXBean.value.uptime.milliseconds)
        gracefulShutdown.waitForShutdown()
    }

    val runtimeMXBean by bean<RuntimeMXBean> {
        ManagementFactory.getRuntimeMXBean()
    }

    val log by bean {
        logger<ApplicationModule>()
    }

    override fun close() {
        if (jdbcModule.dataSource.isInitialized) jdbcModule.close {}
        if (httpClientModule.httpClient.isInitialized) httpClientModule.close {}
    }
}
