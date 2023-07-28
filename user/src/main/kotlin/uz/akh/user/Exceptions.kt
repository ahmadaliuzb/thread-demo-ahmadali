package uz.akh.user

import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ResourceBundleMessageSource
import java.util.*

sealed class UserServiceException(message: String? = null) : RuntimeException(message) {
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

class UserNotFoundException : UserServiceException() {
    override fun errorType() = ErrorCode.USER_NOT_FOUND
}

class UsernameAlreadyExists : UserServiceException() {
    override fun errorType() = ErrorCode.USERNAME_ALREADY_EXISTS
}

class EmailAlreadyExists : UserServiceException() {
    override fun errorType() = ErrorCode.EMAIL_ALREADY_EXISTS
}

class EmailErrorException : UserServiceException() {
    override fun errorType() = ErrorCode.EMAIL_ERROR
}

class GeneralApiException(val msg: String) : UserServiceException() {
    override fun errorType(): ErrorCode = ErrorCode.GENERAL_API_EXCEPTION
}

class FeignErrorException(val code: Int?, val errorMessage: String?) : UserServiceException() {
    override fun errorType() = ErrorCode.GENERAL_API_EXCEPTION
}


