package it.`is`.all.good.coupon.customer

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import it.`is`.all.good.coupon.SecurityDisabledIntegrationTest
import it.`is`.all.good.coupon.operator.AssignCouponCommand
import it.`is`.all.good.coupon.operator.CouponView
import it.`is`.all.good.coupon.operator.GenerateCouponsCommand
import it.`is`.all.good.coupon.operator.OperatorService
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Transactional
internal class CustomerControllerTest: SecurityDisabledIntegrationTest() {

    @Autowired
    lateinit var operator: OperatorService

    @Autowired
    lateinit var service: CustomerService

    @Test
    fun `when customer consume coupons then all coupons should be consumed together`() {
        operator.generateCoupons(GenerateCouponsCommand(1))[0]
        val assigned = operator.assignCoupon(AssignCouponCommand(SUPER_USERNAME))

        val command = ConsumeCouponsCommand(couponIds = listOf(assigned.couponId))
        val commandJson = jacksonObjectMapper().writeValueAsString(command)

        mvc.perform(
                MockMvcRequestBuilders.post("/customers/consume-coupons")
                        .content(commandJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
                .andExpect(jsonPath("$[0].couponId", `is`(assigned.couponId)))
                .andExpect(jsonPath("$[0].couponStatus", `is`("CONSUMED")))
    }

    //TODO when customer consume coupons and if any invalid coupon contains then should fail to consume together

    @Test
    fun `when customer cancel coupon consumption then consumed coupon should be cancelled`() {
        operator.generateCoupons(GenerateCouponsCommand(1))[0]
        val assigned = operator.assignCoupon(AssignCouponCommand(SUPER_USERNAME))
        val consumed = service.consumeCoupons(SUPER_USERNAME, ConsumeCouponsCommand(listOf(assigned.couponId)))

        val command = CancelCouponConsumptionCommand(consumed[0].couponId)
        val commandJson = jacksonObjectMapper().writeValueAsString(command)

        mvc.perform(
                MockMvcRequestBuilders.post("/customers/cancel-coupon-consumption")
                        .content(commandJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
                .andExpect(jsonPath("$.couponId", `is`(assigned.couponId)))
                .andExpect(jsonPath("$.couponStatus", `is`("CANCELLED")))
    }

    @Test
    fun `when customer get expires coupon with date then return expires coupons`() {
        val expiresAt = LocalDate.now()
        val expiresAtString = DateTimeFormatter.ISO_DATE.format(expiresAt)

        operator.generateCoupons(GenerateCouponsCommand(1))[0]
        operator.assignCoupon(AssignCouponCommand(SUPER_USERNAME, 0))

        mvc.perform(
                MockMvcRequestBuilders.get("/customers/coupons-expires-at?date=$expiresAtString"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
                .andExpect(jsonPath("$", hasSize<CouponView>(1)))
    }
}