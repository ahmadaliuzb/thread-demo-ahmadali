package uz.akh.subscription

data class BaseMessage(val code: Int? = null, val message: String? = null)

data class SubscriptionDto(
    val subscriberId: Long,
    val recipientId: Long,
) {
    companion object {
        fun toDto(entity: Subscription) = entity.run { SubscriptionDto(subscriberId, recipientId) }
    }

    fun toEntity() = Subscription(subscriberId, recipientId)
}


data class UserDto(
    val id: Long?,
    val firstName: String,
    val lastName: String,
    val email: String,
    val username: String
)