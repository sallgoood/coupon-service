package it.`is`.all.good.coupon.customer

import it.`is`.all.good.coupon.CouponService
import it.`is`.all.good.coupon.exception.CouponNotFoundException
import it.`is`.all.good.coupon.operator.CouponView
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class CustomerService(
        val couponService: CouponService
) {

    fun consumeCoupons(username: String, command: ConsumeCouponsCommand): List<CouponView> {
        return couponService.consumeCoupons(command, command.couponIds!!, username).map { CouponView.consumed(it) }
    }

    fun cancelCouponConsumption(username: String, command: CancelCouponConsumptionCommand): CouponView {
        val cancelled = couponService.cancelCouponConsumption(command, username)
        return CouponView.cancelled(cancelled)
    }

    fun queryCouponsExpiresAt(username: String, date: LocalDate?): List<CouponView> {
        val expires = couponService.queryCouponsExpiresAt(username, date)
        return expires.map { CouponView.expires(it) }
    }

}