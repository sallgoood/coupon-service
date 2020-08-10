package it.`is`.all.good.coupon.user

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import it.`is`.all.good.coupon.SecurityEnabledIntegrationTest
import org.hamcrest.Matchers
import org.hamcrest.text.IsEmptyString
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

internal class UserControllerTest : SecurityEnabledIntegrationTest() {

    @Autowired
    lateinit var repository: UserRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var service: UserService

    @BeforeEach
    fun clean() {
        repository.deleteAll()
    }

    @Test
    fun `when user sign up with valid data then token should be returned and user should be persisted`() {
        val command = UserSignUpCommand("myoungsokang@gmail.com", "Password!234", "ROLE_OPERATOR")
        val commandJson = jacksonObjectMapper().writeValueAsString(command)

        mvc.perform(
                MockMvcRequestBuilders.post("/users/sign-up")
                        .content(commandJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
                .andExpect(MockMvcResultMatchers.jsonPath("token", Matchers.not(IsEmptyString.emptyOrNullString())))

        val user = repository.findById("myoungsokang@gmail.com").get()
        assertEquals("ROLE_OPERATOR", user.roles.first().name)
    }

    @Test
    fun `when user sign in with invalid username then 404`() {
        val command = UserSignInCommand("myoungsokang@gmail.com", "Password!234")
        val commandJson = jacksonObjectMapper().writeValueAsString(command)

        mvc.perform(
                MockMvcRequestBuilders.post("/users/sign-in")
                        .content(commandJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `when user sign in with valid credential then token should be returned`() {
        val (username, _, _) = repository.save(User(
                "myoungsokang@gmail.com",
                passwordEncoder.encode("Password!234"),
                mutableListOf(Role.ROLE_OPERATOR)))

        val command = UserSignInCommand(username, "Password!234")
        val commandJson = jacksonObjectMapper().writeValueAsString(command)

        mvc.perform(
                MockMvcRequestBuilders.post("/users/sign-in")
                        .content(commandJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
                .andExpect(MockMvcResultMatchers.jsonPath("token", Matchers.not(IsEmptyString.emptyOrNullString())))
    }

    @Test
    fun `when user use services with insufficient authentication then return 401`() {
        //no header
        mvc.perform(
                MockMvcRequestBuilders.get("/operators/generate-coupon"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized)

        //no bearer token
        mvc.perform(
                MockMvcRequestBuilders.get("/operators/generate-coupon")
                        .header("Authorization", "InvalidValue"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun `when user use services with insufficient authority then return 403`() {
        val driverToken = service.signUpUser(UserSignUpCommand("customer@gmail.com", "Password!234", "ROLE_CUSTOMER")).token

        mvc.perform(
                MockMvcRequestBuilders.get("/operators/generate-coupon")
                        .header("Authorization", "Bearer $driverToken"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @Test
    fun `when user sing up with already existing username then return 422`() {
        service.signUpUser(UserSignUpCommand("customer@gmail.com", "Password!234", "ROLE_CUSTOMER"))

        val command = UserSignUpCommand("customer@gmail.com", "Password!234", "ROLE_OPERATOR")
        val commandJson = jacksonObjectMapper().writeValueAsString(command)

        mvc.perform(
                MockMvcRequestBuilders.post("/users/sign-up")
                        .content(commandJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity)
    }
}

