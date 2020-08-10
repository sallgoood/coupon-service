package it.`is`.all.good.coupon.auth

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import it.`is`.all.good.coupon.user.CustomUserDetailsService
import it.`is`.all.good.coupon.user.Role
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest

@Component
class AuthenticationTokenManager(
        val userDetailsService: CustomUserDetailsService) {

    @Value("\${security.jwt.token.secret-key}")
    lateinit var secretKey: String

    @Value("\${security.jwt.token.expire-length}")
    lateinit var expireMilliseconds: String

    @PostConstruct
    protected fun init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.toByteArray())
    }

    fun authenticate(request: HttpServletRequest): Authentication {
        val token = this.resolveToken(request)
        this.validateToken(token)
        val userDetails = userDetailsService.loadUserByUsername(getUsername(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun createToken(username: String, roles: List<Role>): String {

        val claims = Jwts.claims().setSubject(username)
        claims["auth"] = roles
                .map { s -> SimpleGrantedAuthority(s.authority) }
                .filter { Objects.nonNull(it) }

        val now = Date()
        val validity = Date(now.time.plus(expireMilliseconds.toLong()))

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact()
    }

    fun getUsername(token: String): String {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject
    }

    fun resolveToken(req: HttpServletRequest): String {
        val bearerToken = req.getHeader(AUTH_HEADER_NAME)

        require(!bearerToken.isNullOrBlank()) { "required header $AUTH_HEADER_NAME is missing" }
        require(bearerToken.startsWith(BEARER_PREFIX)) { "missing bearer token" }

        return bearerToken.substring(BEARER_PREFIX.length)
    }

    fun validateToken(token: String) {
        Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
    }

    companion object {
        const val AUTH_HEADER_NAME = "Authorization"
        const val BEARER_PREFIX = "Bearer "
    }
}