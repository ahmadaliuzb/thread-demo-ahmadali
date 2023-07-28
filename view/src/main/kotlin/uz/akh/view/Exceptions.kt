package uz.akh.view

import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ResourceBundleMessageSource
import java.util.*

sealed class ViewServiceException(message: String? = null) : RuntimeException(message) {
    abstract fun errorType(): ErrorCode

    fun getErrorMessage(errorMessageSource: ResourceBundleMessageSource, vararg array: Any?): BaseMessage {
        return BaseMessage(
            errorType().code,
            errorMessageSource.getMessage(
                errorType().toString(),
                array,
                Locale(LocaleContextHolder.getLocale().language)
            )
        )
    }
}

class UserNotFoundException : ViewServiceException() {
    override fun errorType() = ErrorCode.USER_NOT_FOUND
}

class PostNotFoundException : ViewServiceException() {
    override fun errorType() = ErrorCode.POST_NOT_FOUND
}

class ViewAlreadyExistsException : ViewServiceException() {
    override fun errorType() = ErrorCode.VIEW_ALREADY_EXISTS
}

class ViewNotFoundException : ViewServiceException() {
    override fun errorType() = ErrorCode.VIEW_NOT_FOUND
}

class GeneralApiException(val msg: String) : ViewServiceException() {
    override fun errorType(): ErrorCode = ErrorCode.GENERAL_API_EXCEPTION
}

class FeignErrorException(val code: Int?, val errorMessage: String?) : ViewServiceException() {
    override fun errorType() = ErrorCode.GENERAL_API_EXCEPTION
}


