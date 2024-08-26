package dev.leachryan.echo.operation

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange

@HttpExchange(url = "/echo", accept = [MediaType.APPLICATION_JSON_VALUE])
interface EchoExchange {

    @Operation(
        tags = ["Echo"],
        summary = "Echo the status code of the request"
    )
    @GetExchange("/{statusCode}")
    fun echo(@PathVariable statusCode: Int): ResponseEntity<HttpStatusCode>
}

@RestController
class EchoController: EchoExchange {

    override fun echo(statusCode: Int): ResponseEntity<HttpStatusCode> {
        return try {
            val httpStatus = HttpStatus.valueOf(statusCode)
            ResponseEntity.status(httpStatus.value()).body(httpStatus)
        } catch (e: Exception) {
            ResponseEntity.badRequest().build()
        }
    }

}