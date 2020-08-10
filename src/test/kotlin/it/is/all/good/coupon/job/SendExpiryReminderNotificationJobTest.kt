package it.`is`.all.good.coupon.job

import it.`is`.all.good.coupon.Coupon
import it.`is`.all.good.coupon.CouponService
import it.`is`.all.good.coupon.notification.NotificationService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = ["expiry-reminder-notification-before-days=3"])
internal class SendExpiryReminderNotificationJobTest {

    @Autowired
    lateinit var job: SendExpiryReminderNotificationJob

    @MockBean
    lateinit var sender: NotificationService

    @MockBean
    lateinit var service: CouponService

    @Test
    fun execute() {
        val coupons = listOf(Coupon.new().assign("anyUsername", -3))
        `when`(service.findExpiresBefore(beforeDays = 3))
                .thenReturn(coupons)

        job.execute()

        verify(sender, times(coupons.size))
                .sendNotification("anyUsername", buildMessage(coupons, "anyUsername"))
    }

    private fun buildMessage(coupons: List<Coupon>, username: String): String {
        val expires = coupons.joinToString("\n") { "coupon id : ${it.id}, expires at : ${it.expiresAt}" }
        return """
        hi $username, your coupons will be expired soon!
        $expires""".trimIndent()
    }
}