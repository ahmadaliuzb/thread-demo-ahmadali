package uz.akh.user

import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.IndexColumn
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.jpa.repository.Temporal
import org.springframework.stereotype.Indexed
import java.io.Serializable
import java.util.*
import javax.persistence.*


/**
27/07/2023 - 5:47 PM
Created by Akhmadali
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
class BaseEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @CreatedDate @Temporal(TemporalType.TIMESTAMP) var createdDate: Date? = null,
    @LastModifiedDate @Temporal(TemporalType.TIMESTAMP) var modifiedDate: Date? = null,
    @Column(nullable = false) @ColumnDefault(value = "false") var deleted: Boolean = false
)


@Entity(name = "users")

class User(

    @Column(length = 64, nullable = false) var firstName: String,
    @Column(length = 64, nullable = true) var lastName: String?,
    @Column(length = 64, nullable = true, name = "username") var username: String?,
    @Column(length = 320, nullable = false, name = "email") var email: String,
    var checked: Boolean = false,
    var uuidForEmail: String?


) : BaseEntity(), Serializable