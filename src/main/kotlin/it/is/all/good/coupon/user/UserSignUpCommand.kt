package it.`is`.all.good.coupon.user

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class UserSignUpCommand(
        @field:Email
        val username: String,

        @field:NotBlank
        val password: String,

        @field:Pattern(regexp = "^$|ROLE_CUSTOMER|ROLE_OPERATOR", message = "allowed roles are, ROLE_CUSTOMER, ROLE_OPERATOR")
        val role: String
)
