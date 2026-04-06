import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import infra.config.ConfigModule
import infra.config.decode
import io.heapy.komok.tech.di.delegate.bean
import kotlinx.serialization.Serializable
import utils.close

class JdbcModule(
    private val configModule: ConfigModule,
) : AutoCloseable {
    val dataSource by bean {
        HikariDataSource(
            HikariConfig().also {
                it.dataSourceClassName = "org.postgresql.ds.PGSimpleDataSource"
                it.username = "awesome_kotlin"
                it.password = "awesome_kotlin"
                it.addDataSourceProperty("databaseName", "awesome_kotlin")
                it.addDataSourceProperty("serverName", jdbcConfig.value.host)
                it.addDataSourceProperty("portNumber", jdbcConfig.value.port)
            }
        )
    }

    val jdbcConfig by bean<JdbcConfig> {
        configModule.decode("jdbc")
    }

    @Serializable
    data class JdbcConfig(
        val host: String,
        val port: String,
    )

    override fun close() {
        if (dataSource.isInitialized) dataSource.value.close {}
    }
}
