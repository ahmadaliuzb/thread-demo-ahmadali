package uz.akh.subscription

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.util.UUID

@FeignClient(name = "user")
interface UserService {
    @GetMapping("internal/exists/{id}")
    fun existById(@PathVariable id: Long): Boolean

    @GetMapping("{id}")
    fun getUserById(@PathVariable id: Long): UserDto
}

interface SubscriptionService {
    fun create(dto: SubscriptionDto)
    fun getById(id: Long): SubscriptionDto
    fun existById(id: Long): Boolean
    fun cancel(id: Long)
    fun getAllSubscriptionBySubscriberId(id: Long): List<Subscription>
}

@Service
class SubscriptionServiceImpl(
    private val repository: SubscriptionRepository,
    private val userService: UserService
) : SubscriptionService {
    @Transactional
    override fun create(dto: SubscriptionDto) {
        //Bu obuna bo'layotgan userni(subscriber) user databaseda bormi yo'qmi tekshiradi
        if (!userService.existById(dto.subscriberId)) throw SubscriberNotFoundException()

        //Bu obuna bo'linayotgan userni(recipient) user databaseda bormi yo'qmi tekshiradi
        if (!userService.existById(dto.recipientId)) throw RecipientNotFoundException()

        //Bu subscriber recipientga obuna bo'lganmi yoki bo'lmaganmi shuni tekshiradi
        if (repository.existsBySubscriberIdAndRecipientIdAndDeletedFalse(
                dto.subscriberId,
                dto.recipientId
            )
        ) throw SubscriptionAlreadyExistsException()

        val subscription: Subscription
        //Bu subscriber oldin recipientga obuna bo'lib keyin obunani bekor qilganligini tekshiradi
        if (repository.existsBySubscriberIdAndRecipientIdAndDeletedTrue(dto.subscriberId, dto.recipientId)) {
            subscription = repository.findBySubscriberIdAndDeletedTrue(dto.subscriberId)
            subscription.deleted = false
        } else {
            subscription = dto.toEntity()
        }

        repository.save(subscription)
    }

    override fun getById(id: Long) = repository.findByIdAndDeletedFalse(id)?.run { SubscriptionDto.toDto(this) }
        ?: throw SubscriptionNotFoundException()

    override fun existById(id: Long): Boolean = repository.existsByIdAndDeletedFalse(id)


    override fun cancel(id: Long) {
        if (!repository.existsByIdAndDeletedFalse(id)) throw SubscriptionNotFoundException()
        val subscription = repository.findByIdAndDeletedFalse(id)
        subscription!!.deleted = true
        repository.save(subscription)
    }

    override fun getAllSubscriptionBySubscriberId(id: Long): List<Subscription> {
        return repository.findAllBySubscriberIdAndDeletedFalse(id)
    }


}

