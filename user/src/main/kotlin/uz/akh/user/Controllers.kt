package uz.akh.user

import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@ControllerAdvice
class ExceptionHandlers(
    private val errorMessageSource: ResourceBundleMessageSource
) {
    @ExceptionHandler(UserServiceException::class)
    fun handleException(exception: UserServiceException): ResponseEntity<*> {
        return when (exception) {
            is UserNotFoundException -> ResponseEntity.badRequest().body(
                exception.getErrorMessage(errorMessageSource, emptyArray<Any>())
            )

            is EmailErrorException -> ResponseEntity.badRequest().body(
                exception.getErrorMessage(errorMessageSource)
            )

            is UsernameAlreadyExists -> ResponseEntity.badRequest().body(
                exception.getErrorMessage(errorMessageSource)
            )

            is EmailAlreadyExists -> ResponseEntity.badRequest().body(
                exception.getErrorMessage(errorMessageSource)
            )

            is FeignErrorException -> ResponseEntity.badRequest().body(
                BaseMessage(exception.code, exception.errorMessage)
            )

            is GeneralApiException -> ResponseEntity.badRequest().body(
                exception.getErrorMessage(errorMessageSource, exception.msg)
            )

        }
    }
}


@RestController
class UserController(private val service: UserService) {
    @PostMapping
    fun create(@RequestBody dto: UserDto) = service.create(dto)

    @GetMapping("{id}")
    fun getById(@PathVariable id: Long) = service.getById(id)

    @GetMapping("checking/{uuidForEmail}")
    fun acceptEmail(@PathVariable uuidForEmail: String) {
        service.acceptEmail(uuidForEmail)
    }
}

@RestController
@RequestMapping("internal")
class UserInternalController(private val service: UserService) {
    @GetMapping("exists/{id}")
    fun existById(@PathVariable id: Long) = service.existById(id)
}