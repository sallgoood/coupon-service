package it.`is`.all.good.coupon.customer

import javax.validation.constraints.NotBlank

data class CancelCouponConsumptionCommand(
    @field:NotBlank
    val couponId: String?
)