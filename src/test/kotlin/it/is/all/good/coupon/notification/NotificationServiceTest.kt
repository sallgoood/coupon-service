package it.`is`.all.good.coupon.notification

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = ["expiry-reminder-notification-before-days=3"])
@Transactional
internal class NotificationServiceTest {

    @Autowired
    lateinit var service: NotificationService

    @Autowired
    lateinit var notifications: NotificationRepository

    @Test
    fun sendNotification() {
        service.sendNotification("anyUser", "anyMessage")
        val (_, sendTo, message, sentAt) = notifications.findAll()[0]
        assertEquals("anyUser", sendTo)
        assertEquals("anyMessage", message)
        assertNotNull(sentAt)
    }
}