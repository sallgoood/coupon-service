package it.`is`.all.good.coupon.user

import org.springframework.security.core.GrantedAuthority

enum class Role : GrantedAuthority {
    ROLE_CUSTOMER {
        override fun getAuthority(): String {
            return "ROLE_CUSTOMER"
        }
    },
    ROLE_OPERATOR {
        override fun getAuthority(): String {
            return "ROLE_OPERATOR"
        }
    }
}
