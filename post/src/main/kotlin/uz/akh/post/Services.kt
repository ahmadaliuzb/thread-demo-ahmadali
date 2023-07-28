package uz.akh.post

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody


@FeignClient(name = "user")
interface UserService {
    @GetMapping("internal/exists/{id}")
    fun existById(@PathVariable id: Long): Boolean

    @GetMapping("{id}")
    fun getUserById(@PathVariable id: Long): UserDto
}

@FeignClient(name = "view")
interface ViewService {
    @GetMapping("internal/exists/{id}")
    fun existById(@PathVariable id: Long): Boolean

    @PostMapping("internal/exists")
    fun existsByUserIdAndPostId(@RequestBody viewDtoForRequest: ViewDtoForRequest): Boolean

    @GetMapping("{id}")
    fun getUserById(@PathVariable id: Long): UserDto

    @PostMapping("create")
    fun create(@RequestBody viewCreateDto: ViewCreateDto)
}

@FeignClient(name = "subscription")
interface SubscriptionService {
    @GetMapping("internal/exists/{id}")
    fun existById(@PathVariable id: Long): Boolean

    @GetMapping("internal/allSubscription/{id}")
    fun getAllSubscriptionBySubscriberId(@PathVariable id: Long): List<SubscriptionDto>
}

interface PostService {
    fun create(dto: PostCreateDto)
    fun getById(id: Long): PostDto
    fun getAllBySubscriberId(pageable: Pageable, id: Long): Page<Post>
}

@Service
class PostServiceImpl(
    private val repository: PostRepository,
    private val userService: UserService,
    private val subscriptionService: SubscriptionService,
    private val viewService: ViewService,
) : PostService {
    @Transactional
    override fun create(dto: PostCreateDto) {
        if (!userService.existById(dto.userId)) throw UserNotFoundException()

        //---

        repository.save(dto.toEntity())
    }

    override fun getById(id: Long): PostDto {
        val post = repository.findByIdAndDeletedFalse(id) ?: throw PostNotFoundException()
        val user = userService.getUserById(post.userId)
        return PostDto.toDto(post, user)
    }

    override fun getAllBySubscriberId(pageable: Pageable, id: Long): Page<Post> {

        val allSubscription = subscriptionService.getAllSubscriptionBySubscriberId(id)
        var counter = 0
        var postList = mutableListOf<Post>()
        for (subscription in allSubscription) {
            val allPost = repository.findAllByUserIdOrderByLikes(pageable, subscription.recipientId)
            for (post in allPost) {
                if (!viewService.existsByUserIdAndPostId(ViewDtoForRequest(id, post.id!!))) {
                    postList.add(post)
                    if (counter < pageable.pageSize) {
                        post.views += 1
                        counter++
                        viewService.create(ViewCreateDto(id, post.id!!, false))
                    }
                    repository.save(post)
                }

            }

        }


        return PageImpl(postList)
    }
}