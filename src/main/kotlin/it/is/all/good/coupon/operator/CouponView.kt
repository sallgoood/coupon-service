package it.`is`.all.good.coupon.operator

import com.fasterxml.jackson.annotation.JsonInclude
import it.`is`.all.good.coupon.Coupon
import it.`is`.all.good.coupon.CouponStatus
import it.`is`.all.good.coupon.CouponStatus.*

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class CouponView(
        val username: String?,
        val couponId: String,
        val couponStatus: CouponStatus
) {
    companion object {
        fun assigned(coupon: Coupon): CouponView {
            assert(ASSIGNED == coupon.status)
            return map(coupon)
        }

        fun new(coupon: Coupon): CouponView {
            assert(NEW == coupon.status)
            return map(coupon)
        }

        fun consumed(coupon: Coupon): CouponView {
            assert(CONSUMED == coupon.status)
            return map(coupon)
        }

        fun cancelled(coupon: Coupon): CouponView {
            assert(CANCELLED == coupon.status)
            return map(coupon)
        }

        fun expires(coupon: Coupon): CouponView {
            return map(coupon)
        }

        private fun map(coupon: Coupon): CouponView {
            return CouponView(
                    username = coupon.username,
                    couponId = coupon.id!!,
                    couponStatus = coupon.status
            )
        }
    }
}