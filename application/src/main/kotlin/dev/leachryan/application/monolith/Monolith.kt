package dev.leachryan.application.monolith

import dev.leachryan.actuator.ActuatorApplication
import dev.leachryan.echo.EchoApplication
import org.springframework.boot.Banner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources
import java.util.Properties

@SpringBootApplication(
    exclude = [
        FlywayAutoConfiguration::class
    ]
)
@PropertySources(
    PropertySource("classpath:application.properties")
)
@EnableConfigurationProperties(MonolithConfigurationProperties::class)
class Monolith

fun main(args: Array<String>) {
    run(args)
}

fun run(
    cliArgs: Array<String>,
    profiles: Array<String> = arrayOf(),
    properties: Properties = Properties(),
    domainInitializers: Array<ApplicationContextInitializer<*>> = arrayOf()
): RuntimeContext {

    var domainBuilder = SpringApplicationBuilder()

    if (profiles.any()) {
        domainBuilder = domainBuilder.profiles(*profiles)
    }

    if (properties.any()) {
        domainBuilder = domainBuilder.properties(properties)
    }

    val domain = domainBuilder
        .sources(
            // Parent
            Monolith::class.java,

            // Infrastructure

            // Configuration

            // Domains

            // Application Services

            // Event Processors

            // Workflow Engine

            // Integrations
        ).bannerMode(Banner.Mode.LOG)
        .beanNameGenerator(FullyQualifiedAnnotationBeanNameGenerator())
        .web(WebApplicationType.NONE)

    // Domain builders
    val echoBuilder = domain.child(EchoApplication::class.java)
        .beanNameGenerator(FullyQualifiedAnnotationBeanNameGenerator())
        .bannerMode(Banner.Mode.OFF)
        .web(WebApplicationType.SERVLET)
        .properties(mapOf(
            "server.port" to 8001,
            "spring.threads.virtual.enabled" to true,
            "springdoc.api-docs.enabled" to true,
            "springdoc.api-docs.path" to "/openapi.json",
            "dev.leachryan.echo.open-api.title" to "Echo Demo REST API",
            "dev.leachryan.echo.open-api.description" to "Echo Demo REST API"
        ))

    val actuatorBuilder = domain.child(ActuatorApplication::class.java)
        .beanNameGenerator(FullyQualifiedAnnotationBeanNameGenerator())
        .bannerMode(Banner.Mode.OFF)
        .web(WebApplicationType.SERVLET)
        .properties(mapOf(
            "server.port" to 9000,
            "management.server.port" to 9000,
            "management.endpoints.health.enabled" to true,
            "spring.threads.virtual.enabled" to true
        ))

    // Profiles
    if (profiles.any()) {
        // builder profiles
        echoBuilder.properties(*profiles)
        actuatorBuilder.properties(*profiles)
    }

    // Properties
    if (properties.any()) {
        // builder properties
        echoBuilder.properties(properties)
        actuatorBuilder.properties(properties)
    }

    // Domain
    if (domainInitializers.any()) {
        domain.initializers(*domainInitializers)
    }
    val domainContext = domain.run(*cliArgs)

    // APIs
    val echoApplicationContext = echoBuilder.run(*cliArgs)
    val actuatorApplicationContext = actuatorBuilder.run(*cliArgs)

    return RuntimeContext(
        domain = domainContext,
        echoApplication = echoApplicationContext,
        actuatorApplication = actuatorApplicationContext
    )
}