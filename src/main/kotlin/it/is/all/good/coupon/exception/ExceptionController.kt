package it.`is`.all.good.coupon.exception

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.persistence.EntityNotFoundException
import javax.security.auth.login.AccountNotFoundException

@RestControllerAdvice
class ExceptionController : ResponseEntityExceptionHandler() {

    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return ResponseEntity.status(BAD_REQUEST).body(ErrorMessage(ex.message))
    }

    @ExceptionHandler(value = [ObjectOptimisticLockingFailureException::class])
    fun handleObjectOptimisticLockingFailureException(ex: ObjectOptimisticLockingFailureException, request: WebRequest): ResponseEntity<Any> {
        return ResponseEntity.status(CONFLICT).body(ErrorMessage(ex.message!!))
    }

    @ExceptionHandler(value = [UsernameExistsException::class])
    fun handleUsernameExistsException(ex: UsernameExistsException, request: WebRequest): ResponseEntity<Any> {
        return ResponseEntity.status(UNPROCESSABLE_ENTITY).body(ErrorMessage(ex.message!!))
    }

    @ExceptionHandler(value = [CouponNotFoundException::class])
    fun handleCouponNotFoundException(ex: CouponNotFoundException, request: WebRequest): ResponseEntity<Any> {
        return ResponseEntity.status(NOT_FOUND).body(ErrorMessage(ex.message!!))
    }

    @ExceptionHandler(value = [NoCouponLeftException::class])
    fun handleNoCouponLeftException(ex: NoCouponLeftException, request: WebRequest): ResponseEntity<Any> {
        return ResponseEntity.status(UNPROCESSABLE_ENTITY).body(ErrorMessage(ex.message!!))
    }

    @ExceptionHandler(value = [IllegalStateException::class])
    fun handleHttpStatusCodeException(ex: IllegalStateException, request: WebRequest): ResponseEntity<Any> {
        return ResponseEntity.status(UNPROCESSABLE_ENTITY).body(ErrorMessage(ex.message!!))
    }

    @ExceptionHandler(value = [EntityNotFoundException::class])
    fun handleHttpStatusCodeException(ex: EntityNotFoundException, request: WebRequest): ResponseEntity<Any> {
        return ResponseEntity.status(UNPROCESSABLE_ENTITY).body(ErrorMessage(ex.message!!))
    }

    @ExceptionHandler(value = [AccountNotFoundException::class])
    fun handleAccountNotFoundException(ex: AccountNotFoundException, request: WebRequest): ResponseEntity<Any> {
        return ResponseEntity.status(NOT_FOUND).body(ErrorMessage(ex.message!!))
    }

    @ExceptionHandler(value = [IllegalArgumentException::class])
    fun handleIllegalArgumentException(ex: IllegalArgumentException, request: WebRequest): ResponseEntity<Any> {
        return ResponseEntity.status(BAD_REQUEST).body(ErrorMessage(ex.message!!))
    }

    @ExceptionHandler(value = [UnAuthorizedException::class])
    fun handleUnAuthorizedException(ex: UnAuthorizedException, request: WebRequest): ResponseEntity<Any> {
        return ResponseEntity.status(UNAUTHORIZED).body(ErrorMessage(ex.message!!))
    }
    override fun handleBindException(ex: BindException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return ResponseEntity.status(BAD_REQUEST).body(ErrorMessage(ex.message))
    }
}

class ErrorMessage(var errors: List<String>) {

    constructor(error: String) : this(listOf<String>(error))

    constructor(vararg errors: String) : this(listOf<String>(*errors))
}
