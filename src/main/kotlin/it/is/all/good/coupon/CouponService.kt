package it.`is`.all.good.coupon

import it.`is`.all.good.coupon.CouponStatus.*
import it.`is`.all.good.coupon.customer.CancelCouponConsumptionCommand
import it.`is`.all.good.coupon.customer.ConsumeCouponsCommand
import it.`is`.all.good.coupon.exception.CouponNotFoundException
import it.`is`.all.good.coupon.exception.NoCouponLeftException
import it.`is`.all.good.coupon.operator.AssignCouponCommand
import it.`is`.all.good.coupon.operator.GenerateCouponsCommand
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class CouponService(
        val coupons: CouponRepository
) {

    @Transactional
    @Retryable(OptimisticLockingFailureException::class)
    fun assignNewCoupon(command: AssignCouponCommand): Coupon {
        val newCoupon = coupons.findFirstByStatusIn(listOf(CouponStatus.NEW, CANCELLED))
                ?: throw NoCouponLeftException("username: ${command.username}")
        return coupons.save(newCoupon.assign(command.username!!, command.expireDays))
    }

    @Transactional
    fun generateCoupons(command: GenerateCouponsCommand): List<Coupon> {
        val newCoupons = IntRange(1, command.number!!)
                .map { Coupon.new() }

        return coupons.saveAll(newCoupons)
    }

    fun getAssignedCoupons(usernames: Set<String>?): List<Coupon> {
        return if (usernames.isNullOrEmpty()) {
            coupons.findByStatus(ASSIGNED)
        } else {
            coupons.findByUsernameIn(usernames)
        } ?: emptyList()
    }

    @Transactional
    fun consumeCoupons(command: ConsumeCouponsCommand, couponIds: List<String>, username: String): MutableList<Coupon> {
        val consumable = coupons.findAllById(command.couponIds!!)
        if (consumable.isNullOrEmpty()) throw CouponNotFoundException(couponIds.toString())
        if (consumable.any { !it.consumable(username) }) throw throw IllegalStateException("username: ${username}, couponIds: ${command.couponIds}")
        return coupons.saveAll(consumable.map { it.consume() })
    }


    @Transactional
    fun cancelCouponConsumption(command: CancelCouponConsumptionCommand, username: String): Coupon {
        val coupon = coupons.findById(command.couponId!!).orElseThrow { throw CouponNotFoundException(command.couponId) }
        if (!coupon.cancelable(username)) throw throw IllegalStateException("username: ${username}, couponId: ${command.couponId}")
        return coupons.save(coupon.cancel())
    }

    @Transactional
    fun queryCouponsExpiresAt(username: String, date: LocalDate?): List<Coupon> {
        return coupons.findByUsernameAndExpiresAt(username, date ?: LocalDate.now()) ?: emptyList()
    }

    fun findExpiresBefore(beforeDays: Long): List<Coupon> {
        val today = LocalDate.now()
        return coupons.findByExpiresAtAndStatusIs(today.minusDays(beforeDays), ASSIGNED) ?: emptyList()
    }
}