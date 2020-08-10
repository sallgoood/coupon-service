package it.`is`.all.good.coupon.user

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class UserController(
        val service: UserService) {

    @PostMapping("/users/sign-up")
    fun signUpUser(@Valid @RequestBody command: UserSignUpCommand): TokenResponse {
        return service.signUpUser(command)
    }

    @PostMapping("/users/sign-in")
    fun signInUser(@Valid @RequestBody command: UserSignInCommand): TokenResponse {
        return service.signInUser(command)
    }
}
