package it.`is`.all.good.coupon

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.orm.ObjectOptimisticLockingFailureException
import java.time.LocalDateTime.now


@SpringBootTest
internal class CouponRepositoryTest {

    @Autowired
    lateinit var repository: CouponRepository

    @Test
    fun `coupon assign with optimistic locking`() {
        val newCoupon = repository.save(Coupon.new())

        val request01 = repository.findById(newCoupon.id!!).get()
        val request02 = repository.findById(newCoupon.id!!).get()

        assertEquals(request01.id, request02.id)

        repository.save(request01.assign("user01"))

        assertThrows(ObjectOptimisticLockingFailureException::class.java)
        { repository.save(request02.assign("user02")) }
    }
}
