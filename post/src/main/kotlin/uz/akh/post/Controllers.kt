package uz.akh.post

import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@ControllerAdvice
class ExceptionHandlers(
    private val errorMessageSource: ResourceBundleMessageSource
) {
    @ExceptionHandler(PostServiceException::class)
    fun handleException(exception: PostServiceException): ResponseEntity<*> {
        return when (exception) {
            is PostNotFoundException -> ResponseEntity.badRequest().body(
                exception.getErrorMessage(errorMessageSource)
            )

            is UserNotFoundException -> ResponseEntity.badRequest().body(
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
class PostController(private val service: PostService) {
    @PostMapping
    fun create(@RequestBody dto: PostCreateDto) = service.create(dto)

    @GetMapping("{id}")
    fun getById(@PathVariable id: Long) = service.getById(id)

    @GetMapping(
        "allPost/{userId}"
    )
    fun getAllPostByUserId(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "5") size: Int,
        @PathVariable userId: Long
    ) {
        val pageable: PageRequest = PageRequest.of(page, size)
        service.getAllBySubscriberId(pageable, userId)
    }
}
