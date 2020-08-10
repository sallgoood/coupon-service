package it.`is`.all.good.coupon.notification

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table
data class Notification(
        @Id
        @GeneratedValue
        var id: Long? = null,

        @Column
        val sendTo: String,

        @Column
        val message: String,

        @Column
        var sentAt: LocalDateTime? = null
)