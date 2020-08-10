package it.`is`.all.good.coupon.customer

import it.`is`.all.good.coupon.operator.CouponView
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import javax.validation.Valid

@RestController
class CustomerController(
        val service: CustomerService
) {

    //필수 문제 4
    @PostMapping("/customers/consume-coupons")
    fun consumeCoupons(
            @RequestHeader("Authorization", defaultValue = "Auth") authToken: String,
            @Valid @RequestBody command: ConsumeCouponsCommand,
                       authentication: Authentication): List<CouponView> {
        val customer = authentication.principal as UserDetails
        return service.consumeCoupons(customer.username, command)
    }

    //필수 문제 5
    @PostMapping("/customers/cancel-coupon-consumption")
    fun cancelCouponConsumption(
            @RequestHeader("Authorization", defaultValue = "Auth") authToken: String,
            @Valid @RequestBody command: CancelCouponConsumptionCommand,
                                authentication: Authentication): CouponView {
        val customer = authentication.principal as UserDetails
        return service.cancelCouponConsumption(customer.username, command)
    }

    //필수 문제 6
    @GetMapping("/customers/coupons-expires-at")
    fun queryCouponsExpiresAt(
            @RequestHeader("Authorization", defaultValue = "Auth") authToken: String,
            @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) date: LocalDate?,
                              authentication: Authentication): List<CouponView> {
        val customer = authentication.principal as UserDetails
        return service.queryCouponsExpiresAt(customer.username, date)
    }
}

