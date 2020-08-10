package it.`is`.all.good.coupon.auth

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.NegatedRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(val tokenManager: AuthenticationTokenManager) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http!!.csrf().disable()

        http.sessionManagement().sessionCreationPolicy(STATELESS)

        http.authorizeRequests()
                .antMatchers("/users/sign-in").permitAll()
                .antMatchers("/users/sign-up").permitAll()

                .antMatchers("/actuator/**").permitAll()

                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/v3/api-docs/**").permitAll()

                .antMatchers("/operators/**").hasRole("OPERATOR")
                .antMatchers("/customers/**").hasRole("CUSTOMER")
                .anyRequest().authenticated()

                .and()

                .addFilterBefore(JwtTokenFilter(tokenManager, NegatedRequestMatcher(OrRequestMatcher(
                        AntPathRequestMatcher("/users/**"),
                        AntPathRequestMatcher("/actuator/**"),
                        AntPathRequestMatcher("/v3/api-docs/**"),
                        AntPathRequestMatcher("/swagger-ui/**"),
                        AntPathRequestMatcher("/swagger-ui.html")
                ))),
                        UsernamePasswordAuthenticationFilter::class.java)
    }

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean("authenticationManager")
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }
}
