package uz.akh.view

import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@ControllerAdvice
class ExceptionHandlers(
    private val errorMessageSource: ResourceBundleMessageSource
) {
    @ExceptionHandler(ViewServiceException::class)
    fun handleException(exception: ViewServiceException): ResponseEntity<*> {
        return when (exception) {
            is UserNotFoundException -> ResponseEntity.badRequest().body(
                exception.getErrorMessage(errorMessageSource, emptyArray<Any>())
            )

            is PostNotFoundException -> ResponseEntity.badRequest().body(
                exception.getErrorMessage(errorMessageSource)
            )

            is ViewAlreadyExistsException -> ResponseEntity.badRequest().body(
                exception.getErrorMessage(errorMessageSource)
            )

            is ViewNotFoundException -> ResponseEntity.badRequest().body(
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
class ViewController(private val service: ViewService) {
    @PostMapping("create")
    fun create(@RequestBody dto: ViewDto) = service.create(dto)

    @GetMapping("{id}")
    fun getById(@PathVariable id: Long) = service.getById(id)


}

@RestController
@RequestMapping("internal")
class ViewInternalController(private val service: ViewService) {
    @GetMapping("exists/{id}")
    fun existById(@PathVariable id: Long): Boolean = service.existById(id)

    @PostMapping("exists")
    fun existsByUserIdAndPostId(@RequestBody viewDto: ViewDto): Boolean =
        service.existByUserIdAndPostId(viewDto.userId, viewDto.postId)


}