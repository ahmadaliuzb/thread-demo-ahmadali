package uz.akh.user

data class BaseMessage(val code: Int? = null, val message: String? = null)

data class UserDto(
    val id: Long?,
    val firstName: String,
    val lastName: String?,
    val username: String?,
    val email: String,
) {
    companion object {
        fun toDto(entity: User) = entity.run { UserDto(id, firstName, lastName, username, email) }
    }

    fun toEntity() = User(firstName, lastName, username, email, false, null)
}

