package uz.akh.view

enum class ErrorCode(val code: Int) {
    USER_NOT_FOUND(400),
    POST_NOT_FOUND(401),
    GENERAL_API_EXCEPTION(402),
        VIEW_ALREADY_EXISTS(404),
    VIEW_NOT_FOUND(405),
}