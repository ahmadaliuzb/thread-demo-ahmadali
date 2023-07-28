package uz.akh.post

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BaseMessage(var code: Int? = null, var message: String? = null)

data class PostCreateDto(
    val text: String,
    val userId: Long,

    ) {
    fun toEntity() = Post(text, userId, 0, 0)
}


data class PostDto(
    val text: String,
    val user: UserDto,
    val likes: Int,
    val views: Int
) {
    companion object {
        fun toDto(order: Post, user: UserDto) = order.run {
            PostDto(text, user, likes, views)
        }
    }
}

data class UserDto(
    val id: Long?,
    val firstName: String,
    val lastName: String,
    val email: String,
    val username: String
)

data class SubscriptionDto(
    val id: Long?,
    val subscriberId: Long,
    val recipientId: Long,
)

data class ViewDtoForRequest(
    val userId:Long,
    val postId:Long
)

data class ViewDtoForResponse(
    val id:Long?,
    val userId:Long,
    val postId:Long
)
data class ViewCreateDto(
    val userId: Long,
    val postId: Long,
    var liked: Boolean?
)