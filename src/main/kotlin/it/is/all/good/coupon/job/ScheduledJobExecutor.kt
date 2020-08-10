package it.`is`.all.good.coupon.job

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ScheduledJobExecutor(
        val job: SendExpiryReminderNotificationJob
) {

    @Scheduled(cron = "0 0 1 * * *")
    fun sendExpiryReminderNotificationToCustomers() = job.execute()

}