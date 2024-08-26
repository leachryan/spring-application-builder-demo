package dev.leachryan.application.monolith

import org.springframework.context.ConfigurableApplicationContext

class RuntimeContext(
    val domain: ConfigurableApplicationContext,
    val echoApplication: ConfigurableApplicationContext,
    val actuatorApplication: ConfigurableApplicationContext
) {
    fun shutdown() {
        domain.stop()
        echoApplication.stop()
        actuatorApplication.stop()
    }
}