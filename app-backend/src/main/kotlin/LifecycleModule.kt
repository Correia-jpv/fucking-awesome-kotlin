import io.heapy.komok.tech.di.delegate.bean
import lifecycle.GracefulShutdown
import lifecycle.JvmShutdownManager
import lifecycle.ShutdownManager

class LifecycleModule {
    val shutdownHandler by bean<ShutdownManager> {
        JvmShutdownManager()
    }

    val gracefulShutdown by bean {
        GracefulShutdown().also {
            shutdownHandler.value.addHandler(it::shutdown)
        }
    }
}