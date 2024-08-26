package dev.leachryan.application.monolith

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.converter.ByteArrayHttpMessageConverter
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Component
class ApplicationWebMvcConfigurer(
    val objectMapper: ObjectMapper
): WebMvcConfigurer {

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.add(ByteArrayHttpMessageConverter())
        converters.add(MappingJackson2HttpMessageConverter(objectMapper))
    }
}