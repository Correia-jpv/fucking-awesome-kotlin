import io.heapy.komok.tech.di.delegate.bean
import org.jooq.SQLDialect
import org.jooq.impl.DSL

open class JooqModule(
    private val jdbcModule: JdbcModule,
) {
    open val dslContext by bean {
        System.setProperty("org.jooq.no-logo", "true")
        System.setProperty("org.jooq.no-tips", "true")
        DSL.using(jdbcModule.dataSource.value, SQLDialect.POSTGRES)
    }
}
