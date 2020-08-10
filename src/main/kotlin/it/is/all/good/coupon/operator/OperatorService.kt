package it.`is`.all.good.coupon.operator

import it.`is`.all.good.coupon.CouponService
import org.springframework.stereotype.Service

@Service
class OperatorService(
        val couponService: CouponService
) {

    fun generateCoupons(command: GenerateCouponsCommand): List<CouponView> {
        return couponService.generateCoupons(command).map { CouponView.new(it) }
    }

    fun assignCoupon(command: AssignCouponCommand): CouponView {
        val assigned = couponService.assignNewCoupon(command)
        return CouponView.assigned(assigned)
    }

    fun getAssignedCoupons(usernames: Set<String>?): List<CouponView> {
        return couponService.getAssignedCoupons(usernames).map { CouponView.assigned(it) }
    }
}
