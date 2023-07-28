package uz.akh.view

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


@Entity(name = "views")
class View(
    val userId: Long,
    val postId: Long,
    var liked: Boolean?

) : BaseEntity()