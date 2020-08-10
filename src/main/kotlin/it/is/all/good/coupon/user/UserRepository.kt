package it.`is`.all.good.coupon.user

import org.springframework.data.jpa.repository.JpaRepository


interface UserRepository : JpaRepository<User, String>
