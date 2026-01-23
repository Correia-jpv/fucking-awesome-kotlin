import io.heapy.komok.tech.di.delegate.bean
import org.flywaydb.core.Flyway

class FlywayModule(
    private val jdbcModule: JdbcModule,
) {
    val flyway by bean<Flyway> {
        Flyway.configure()
            .locations("classpath:db/migration/main")
            .dataSource(jdbcModule.dataSource.value)
            .load()
    }
}