package it.`is`.all.good.coupon.notification

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation.REQUIRES_NEW
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class NotificationService(
        val notifications: NotificationRepository
) {

    @Transactional
    fun sendNotification(username: String, message: String) {
        val notification = persistBeforeSend(username, message)
        doSend(notification)
        notification.sentAt = LocalDateTime.now()
        notifications.save(notification)
    }

    @Transactional(propagation = REQUIRES_NEW)
    fun persistBeforeSend(username: String, message: String) = notifications.save(Notification(sendTo = username, message = message))

    fun doSend(notification: Notification) {
        println(notification.message)
    }
}