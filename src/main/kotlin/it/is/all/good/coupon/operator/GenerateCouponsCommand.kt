package it.`is`.all.good.coupon.operator

import javax.validation.constraints.Min

data class GenerateCouponsCommand(
        @field:Min(1)
        val number: Int?
)