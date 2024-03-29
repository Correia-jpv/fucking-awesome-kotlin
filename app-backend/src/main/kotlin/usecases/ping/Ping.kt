package usecases.ping

import io.ktor.server.application.call
import io.ktor.server.locations.Location
import io.ktor.server.locations.get
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import ktor.KtorRoute

@Location("/ping/{name}")
class PingRequest(val name: String)

class PingRoute : KtorRoute {
    override fun Routing.install() {
        get<PingRequest> {
            call.respondText("Pong: ${it.name}")
        }
    }
}

