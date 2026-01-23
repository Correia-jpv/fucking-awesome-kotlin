package usecases.ping

import io.heapy.komok.tech.di.delegate.bean

class PingModule {
    val route by bean {
        PingRoute()
    }
}
