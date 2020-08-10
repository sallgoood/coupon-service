package it.`is`.all.good.coupon.user

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
        val repository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        val (_, password, roles) = repository.findById(username!!)
                .orElseThrow { throw UsernameNotFoundException("$username not found") }

        return User
                .withUsername(username)
                .password(password)
                .authorities(roles)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build()
    }
}
