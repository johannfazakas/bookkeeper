package ro.jf.bk.account.api.web.error

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.multipart.MultipartException

private val log = KotlinLogging.logger { }

@ControllerAdvice
class AccountWebErrorHandler {
    @ExceptionHandler(MultipartException::class)
    fun zoneRulesException(e: MultipartException): ResponseEntity<ProblemDetail> {
        log.warn("Error on multipart upload.", e)

        val statusCode = HttpStatus.BAD_REQUEST.value()
        return ResponseEntity
            .status(statusCode)
            .header("Content-Type", "application/problem+json")
            .body(
                ProblemDetail.forStatus(statusCode)
                    .apply {
                        title = "Multipart upload error"
                        detail = e.message
                    }
            )
    }
}