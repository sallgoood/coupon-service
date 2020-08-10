package it.`is`.all.good.coupon.notification

import org.springframework.data.jpa.repository.JpaRepository

interface NotificationRepository: JpaRepository<Notification, Long> {

    fun findBySendToAndSentAtIsNull(username: String)
}