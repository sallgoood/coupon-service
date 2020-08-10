package it.`is`.all.good.coupon.user

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class UserSignInCommand(
        @field:Email
        val username: String?,

        @field:NotBlank
        val password: String?
)
