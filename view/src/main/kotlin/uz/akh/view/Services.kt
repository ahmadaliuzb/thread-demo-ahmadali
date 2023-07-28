package uz.akh.view

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

@FeignClient(name = "post")
interface PostService {
    @GetMapping("internal/exists/{id}")
    fun existById(@PathVariable id: Long): Boolean

    @GetMapping("{id}")
    fun getUserById(@PathVariable id: Long): UserDto
}

interface ViewService {
    fun create(dto: ViewDto)
    fun getById(id: Long): ViewDto
    fun existById(id: Long): Boolean
    fun existByUserIdAndPostId(userId: Long, postId: Long): Boolean
}

@Service
class ViewServiceImpl(
    private val repository: ViewRepository,
    private val userService: UserService,
    private val postService: PostService
) : ViewService {
    @Transactional
    override fun create(dto: ViewDto) {
        if (!userService.existById(dto.userId)) throw UserNotFoundException()
        if (!postService.existById(dto.postId)) throw PostNotFoundException()
        if (!repository.existsByUserIdAndPostId(dto.userId, dto.postId)) throw ViewAlreadyExistsException()
        repository.save(dto.toEntity())
    }

    override fun getById(id: Long) = repository.findByIdAndDeletedFalse(id)?.run { ViewDto.toDto(this) }
        ?: throw ViewNotFoundException()

    override fun existById(id: Long): Boolean = repository.existsByIdAndDeletedFalse(id)
    override fun existByUserIdAndPostId(userId: Long, postId: Long) = repository.existsByUserIdAndPostId(userId, postId)


}

