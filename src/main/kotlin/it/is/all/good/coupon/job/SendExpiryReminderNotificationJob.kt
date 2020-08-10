package it.`is`.all.good.coupon.job

import it.`is`.all.good.coupon.Coupon
import it.`is`.all.good.coupon.CouponService
import it.`is`.all.good.coupon.notification.NotificationService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SendExpiryReminderNotificationJob(
        val service: CouponService,
        val sender: NotificationService
) {

    @Value("\${expiry-reminder-notification-before-days}")
    lateinit var beforeDays: String

    fun execute() = service.findExpiresBefore(beforeDays.toLong()).groupBy { it.username }
            .entries.forEach { (username, coupons) ->
                val message = buildMessage(coupons, username)
                sender.sendNotification(username!!, message)
            }

    private fun buildMessage(coupons: List<Coupon>, username: String?): String {
        val expires = coupons.joinToString("\n") { "coupon id : ${it.id}, expires at : ${it.expiresAt}" }
        return """
        hi $username, your coupons will be expired soon!
        $expires""".trimIndent()
    }
}