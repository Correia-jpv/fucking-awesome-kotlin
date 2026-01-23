package usecases.signup

import ConfigModule
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.heapy.komok.tech.di.delegate.bean
import kotlinx.serialization.Serializable
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.hocon.decodeFromConfig
import java.util.*

class JwtModule(
    private val configModule: ConfigModule,
) {
    val jwtConfig by bean<JwtConfig> {
        Hocon.decodeFromConfig(configModule.config.value.getConfig("jwt"))
    }

    val generateJwt by bean {
        GenerateJwt(
            jwtConfig = jwtConfig.value,
        )
    }

    @Serializable
    data class JwtConfig(
        val audience: String,
        val realm: String,
        val issuer: String,
        val secret: String,
    )
}

class GenerateJwt(
    private val jwtConfig: JwtModule.JwtConfig,
) {
    operator fun invoke(id: String): String {
        return JWT.create()
            .withAudience(jwtConfig.audience)
            .withIssuer(jwtConfig.issuer)
            .withClaim("id", id)
            .withIssuedAt(Date(System.currentTimeMillis()))
            .sign(Algorithm.HMAC512(jwtConfig.secret))
    }
}
