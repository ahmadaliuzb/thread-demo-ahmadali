package uz.akh.user

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.util.UUID


interface UserService {
    fun create(dto: UserDto)
    fun getById(id: Long): UserDto
    fun existById(id: Long): Boolean
    fun acceptEmail(uuidForEmail: String)
}

@Service
class UserServiceImpl(
    private val repository: UserRepository,
    private val emailSenderService: EmailSenderService
) : UserService {
    override fun create(dto: UserDto) {

        //databaseda email unique ligini taminlash uchun
        if (repository.existsByCheckedTrueAndDeletedFalseAndEmail(dto.email)) throw EmailAlreadyExists()
        //databaseda username unique ligini taminlash uchun
        dto.username?.let {
            if (repository.existsByCheckedTrueAndDeletedFalseAndUsername(it)) throw UsernameAlreadyExists()
        }

        val user = dto.toEntity()
        val uuid: String = UUID.randomUUID().toString()
        user.uuidForEmail = uuid
        emailSenderService.sendSimpleEmail(
            user.email,
            "Click the link below and confirm your email",
            "http://localhost:8080/api/v1/user/checking/$uuid"
        )

        repository.save(user)
    }

    override fun getById(id: Long) = repository.findByIdAndDeletedFalse(id)?.run { UserDto.toDto(this) }
        ?: throw UserNotFoundException()

    override fun existById(id: Long): Boolean {
        return repository.existsByIdAndDeletedFalse(id)
    }

    override fun acceptEmail(uuidForEmail: String) {
        val user = repository.findByCheckedFalseAndUuidForEmail(uuidForEmail)
        user?.run {
            user.checked = true
            repository.save(user)
        } ?: throw UserNotFoundException()
    }

}

@Service
class EmailSenderService(
    private val mailSender: JavaMailSender
) {
    fun sendSimpleEmail(
        toEmail: String?,
        subject: String?,
        body: String?
    ) {
        val message = SimpleMailMessage()
        message.setFrom("secursally@gmail.com")
        message.setTo(toEmail)
        message.setText(body!!)
        message.setSubject(subject!!)
        try {
            mailSender.send(message)
        } catch (e: Exception) {
            throw EmailErrorException()
        }
        println("Mail Send...")
    }
}