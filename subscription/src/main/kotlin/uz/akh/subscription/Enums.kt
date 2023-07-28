package uz.akh.subscription

enum class ErrorCode(val code: Int) {
    SUBSCRIBER_NOT_FOUND(300),
    RECIPIENT_NOT_FOUND(301),
    GENERAL_API_EXCEPTION(302),
        SUBSCRIPTION_ALREADY_EXISTS(304),
    SUBSCRIPTION_NOT_FOUND(305),
}