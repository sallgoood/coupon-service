package it.`is`.all.good.coupon.customer

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

data class ConsumeCouponsCommand(

        @field:NotEmpty
        val couponIds: List<String>?
)