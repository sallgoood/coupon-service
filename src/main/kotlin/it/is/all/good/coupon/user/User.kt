package it.`is`.all.good.coupon.user

import it.`is`.all.good.coupon.user.Role
import javax.persistence.*

@Entity
@Table
data class User(
        @Id
        val username: String,

        @Column(nullable = false)
        val password: String,

        @ElementCollection(fetch = FetchType.EAGER)
        var roles: MutableList<Role>
)
