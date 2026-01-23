package link.kotlin.scripts.module

import io.heapy.komok.tech.di.delegate.bean
import link.kotlin.scripts.scripting.CachingScriptEvaluator
import link.kotlin.scripts.scripting.ScriptEvaluator
import link.kotlin.scripts.scripting.ScriptingScriptEvaluator
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

class ScriptingModule(
    private val utilsModule: UtilsModule,
) {
    val scriptingHost by bean {
        BasicJvmScriptingHost()
    }

    val scriptEvaluator by bean<ScriptEvaluator> {
        val scriptingScriptEvaluator = ScriptingScriptEvaluator(
            scriptingHost = scriptingHost.value
        )

        CachingScriptEvaluator(
            cache = utilsModule.cache.value,
            scriptEvaluator = scriptingScriptEvaluator
        )
    }
}