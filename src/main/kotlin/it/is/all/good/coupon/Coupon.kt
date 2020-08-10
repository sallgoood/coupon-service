package it.`is`.all.good.coupon

import it.`is`.all.good.coupon.CouponStatus.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(indexes = [
    Index(columnList = "username"),
    Index(columnList = "status"),
    Index(columnList = "expiresAt")
])
data class Coupon(
        @Id
        val id: String? = null,

        @Column
        val generatedAt: LocalDateTime,

        @Column
        var status: CouponStatus = NEW,

        @Column
        var username: String? = null,

        @Column
        var assignedAt: LocalDateTime? = null,

        @Column
        var consumedAt: LocalDateTime? = null,

        @Column
        var canceledAt: LocalDateTime? = null,

        @Column
        var expiresAt: LocalDate? = null,

        @Column
        @Version
        var version: Long? = null
) {
    fun assign(username: String, expireDays: Long? = null): Coupon {
        assert(NEW == this.status)
        val now = LocalDateTime.now()
        this.username = username
        this.assignedAt = now
        this.status = ASSIGNED
        this.expiresAt = now.toLocalDate().plusDays(expireDays ?: DEFAULT_EXPIRE_DAYS)
        return this
    }

    fun consumable(username: String) =
            ASSIGNED == this.status
                    && owner(username)
                    && !expired()

    fun consume(): Coupon {
        assert(ASSIGNED == this.status)
        this.status = CONSUMED
        this.consumedAt = LocalDateTime.now()
        return this
    }

    fun cancelable(username: String): Boolean {
        return CONSUMED == this.status
                && owner(username)
                && !expired()
    }

    fun cancel(): Coupon {
        assert(CONSUMED == this.status)
        this.status = CANCELLED
        this.canceledAt = LocalDateTime.now()
        return this
    }

    fun owner(username: String) = username == this.username

    fun expired() = this.expiresAt?.isBefore(LocalDate.now()) ?: false


    companion object {

        const val DEFAULT_EXPIRE_DAYS = 3L

        fun new(): Coupon {
            return Coupon(id = UUID.randomUUID().toString(),
                    generatedAt = LocalDateTime.now(),
                    status = NEW)
        }
    }
}