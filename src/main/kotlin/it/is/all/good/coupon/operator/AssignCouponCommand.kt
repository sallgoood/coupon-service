package it.`is`.all.good.coupon.operator

import com.fasterxml.jackson.annotation.JsonUnwrapped
import javax.validation.constraints.Email

data class AssignCouponCommand(
        @field:Email
        val username: String?,
        var expireDays: Long? = null
)


data class AssignCouponCommandResult(
        @JsonUnwrapped
        val coupon: CouponView
)