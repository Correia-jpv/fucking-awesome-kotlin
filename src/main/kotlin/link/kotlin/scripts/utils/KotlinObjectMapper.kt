package link.kotlin.scripts.utils

import tools.jackson.databind.ObjectMapper
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.kotlinModule

@Suppress("FunctionName")
fun KotlinObjectMapper(): ObjectMapper =
    JsonMapper.builder()
        .addModule(kotlinModule { })
        .build()
