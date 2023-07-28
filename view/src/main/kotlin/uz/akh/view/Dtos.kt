package uz.akh.view

data class BaseMessage(val code: Int? = null, val message: String? = null)

data class ViewDto(
    val userId: Long,
    val postId: Long,
    var liked: Boolean?
) {
    companion object {
        fun toDto(entity: View) = entity.run { ViewDto(userId, postId, liked) }
    }

    fun toEntity() = View(userId, postId, liked)
}


data class UserDto(
    val id: Long?,
    val firstName: String,
    val lastName: String,
    val email: String,
    val username: String
)

data class PostDto(
    val id: Long?,
    val text: String,
    val user: UserDto,
    val likes: Int,
    val views: Int
)