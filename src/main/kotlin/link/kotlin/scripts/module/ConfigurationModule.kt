package link.kotlin.scripts.module

import io.heapy.komok.tech.di.delegate.bean
import link.kotlin.scripts.model.ApplicationConfiguration
import link.kotlin.scripts.model.DataApplicationConfiguration
import link.kotlin.scripts.utils.logger

class ConfigurationModule {
    val applicationConfiguration by bean<ApplicationConfiguration> {
        val configuration = DataApplicationConfiguration()
        if (configuration.ghToken.isEmpty()) {
            logger<DataApplicationConfiguration>().info("GH_TOKEN is not defined, dry run...")
            configuration.copy(dryRun = true)
        } else configuration
    }
}