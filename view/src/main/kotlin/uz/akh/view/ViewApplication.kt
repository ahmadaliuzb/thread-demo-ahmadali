package uz.akh.view

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaAuditing
@EnableFeignClients
@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl::class)
class ViewApplication

fun main(args: Array<String>) {
    runApplication<ViewApplication>(*args)
}
