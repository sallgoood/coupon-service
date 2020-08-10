package it.`is`.all.good.coupon.operator

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import it.`is`.all.good.coupon.CouponStatus.ASSIGNED
import it.`is`.all.good.coupon.SecurityDisabledIntegrationTest
import org.hamcrest.Matchers
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@Transactional
internal class OperatorControllerTest : SecurityDisabledIntegrationTest() {

    @Autowired
    lateinit var service: OperatorService

    @Test
    fun `when operator generate coupon with number then coupons should be generated`() {
        val command = GenerateCouponsCommand(1)
        val commandJson = jacksonObjectMapper().writeValueAsString(command)

        mvc.perform(
                post("/operators/generate-coupons")
                        .content(commandJson)
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful)
                .andExpect(jsonPath("$", hasSize<CouponView>(1)))
    }

    @Test
    fun `when operator assign coupon with username then new coupon should be assigned`() {
        service.generateCoupons(GenerateCouponsCommand(1))

        val command = AssignCouponCommand("username@gmail.com")
        val commandJson = jacksonObjectMapper().writeValueAsString(command)

        mvc.perform(
                post("/operators/assign-coupon")
                        .content(commandJson)
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful)
                .andExpect(jsonPath("couponId", Matchers.any(String::class.java)))
                .andExpect(jsonPath("username", `is`("username@gmail.com")))
                .andExpect(jsonPath("couponStatus", `is`(ASSIGNED.name)))
    }

    @Test
    fun `when operator get assigned coupons with usernames then coupons should be returned`() {
        service.generateCoupons(GenerateCouponsCommand(1))
        val assigned = service.assignCoupon(AssignCouponCommand("username@gmail.com"))

        mvc.perform(
                get("/operators/assigned-coupons?usernames=username@gmail.com"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful)
                .andExpect(jsonPath("$").value(contains(jacksonObjectMapper().convertValue(assigned, Map::class.java))))
    }
}