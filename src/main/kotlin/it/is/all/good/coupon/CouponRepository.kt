package it.`is`.all.good.coupon

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface CouponRepository : JpaRepository<Coupon, String> {

    fun findByUsernameIn(usernames: Iterable<String>): List<Coupon>?

    fun findByUsernameAndExpiresAt(username: String, date: LocalDate): List<Coupon>?

    fun findByExpiresAtAndStatusIs(date: LocalDate, status: CouponStatus): List<Coupon>?

    fun findByStatus(status: CouponStatus): List<Coupon>?

    fun findFirstByStatus(status: CouponStatus): Coupon?

    fun findFirstByStatusIn(status: Iterable<CouponStatus>): Coupon?
}