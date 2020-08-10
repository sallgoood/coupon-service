package it.`is`.all.good.coupon

import it.`is`.all.good.coupon.auth.AuthenticationTokenManager
import it.`is`.all.good.coupon.user.Role
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.User
import org.springframework.test.web.servlet.MockMvc
import javax.servlet.http.HttpServletRequest

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = true)
abstract class SecurityDisabledIntegrationTest {

    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var tokenManager: AuthenticationTokenManager

    @BeforeEach
    fun init() {
        val superUser = User.withUsername(SUPER_USERNAME)
                .password("anyPwd")
                .authorities(listOf(Role.ROLE_OPERATOR, Role.ROLE_CUSTOMER))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build()

        Mockito.`when`(tokenManager.authenticate(any(HttpServletRequest::class.java)))
                .thenReturn(UsernamePasswordAuthenticationToken(superUser, "", superUser.authorities))
    }

    companion object {
        fun <T> any(type: Class<T>): T = Mockito.any(type)

        const val SUPER_USERNAME = "super@super.com"
    }
}
