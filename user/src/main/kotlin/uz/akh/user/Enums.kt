package uz.akh.user

enum class ErrorCode(val code: Int) {
   USER_NOT_FOUND(100),
   EMAIL_ERROR(101),
   GENERAL_API_EXCEPTION(102),
   USERNAME_ALREADY_EXISTS(103),
   EMAIL_ALREADY_EXISTS(104)

}