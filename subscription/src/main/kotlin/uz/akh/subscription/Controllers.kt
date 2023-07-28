package uz.akh.subscription

import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@ControllerAdvice
class ExceptionHandlers(
    private val errorMessageSource: ResourceBundleMessageSource
) {
    @ExceptionHandler(SubscriptionServiceException::class)
    fun handleException(exception: SubscriptionServiceException): ResponseEntity<*> {
        return when (exception) {
            is SubscriptionNotFoundException -> ResponseEntity.badRequest().body(
                exception.getErrorMessage(errorMessageSource, emptyArray<Any>())
            )

            is SubscriberNotFoundException -> ResponseEntity.badRequest().body(
                exception.getErrorMessage(errorMessageSource)
            )

            is SubscriptionAlreadyExistsException -> ResponseEntity.badRequest().body(
                exception.getErrorMessage(errorMessageSource)
            )

            is RecipientNotFoundException -> ResponseEntity.badRequest().body(
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
class SubscriptionController(private val service: SubscriptionService) {
    @PostMapping
    fun create(@RequestBody dto: SubscriptionDto) = service.create(dto)

    @GetMapping("{id}")
    fun getById(@PathVariable id: Long) = service.getById(id)

    @GetMapping("cancel/{id}")
    fun cancel(@PathVariable id: Long) {
        service.cancel(id)
    }
}

@RestController
@RequestMapping("internal")
class SubscriptionInternalController(private val service: SubscriptionService) {
    @GetMapping("exists/{id}")
    fun existById(@PathVariable id: Long): Boolean = service.existById(id)

    @GetMapping("allSubscription/{id}")
    fun getAllSubscriptionBySubscriberId(@PathVariable id: Long): List<Subscription> =
        service.getAllSubscriptionBySubscriberId(id)


}