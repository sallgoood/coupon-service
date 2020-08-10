package it.`is`.all.good.coupon.operator

import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class OperatorController(
        val service: OperatorService
) {

    //필수 문제 1
    @PostMapping("/operators/generate-coupons")
    fun generateCoupons(
            @RequestHeader("Authorization", defaultValue = "Auth") authToken: String,
            @Valid @RequestBody command: GenerateCouponsCommand): List<CouponView> {
        return service.generateCoupons(command)
    }

    //필수 문제2
    @PostMapping("/operators/assign-coupon")
    fun assignCoupon(
            @RequestHeader("Authorization", defaultValue = "Auth") authToken: String,
            @Valid @RequestBody command: AssignCouponCommand): CouponView {
        return service.assignCoupon(command)
    }

    //필수 문제3
    @GetMapping("/operators/assigned-coupons")
    fun queryAssignedCoupons(
            @RequestHeader("Authorization", defaultValue = "you can obtain from /users/sign-up") authToken: String,
            @RequestParam(required = false) usernames: Set<String>?): List<CouponView> {
        return service.getAssignedCoupons(usernames)
    }
}