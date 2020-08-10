package it.`is`.all.good.coupon

enum class CouponStatus {
    NEW,
    ASSIGNED,
    CONSUMED,
    CANCELLED
}

//enum class CouponStatus(val transitions: Set<CouponStatus>) {
//    NEW(setOf(valueOf("ASSIGNED"))),
//    ASSIGNED(setOf(valueOf("CONSUMED"))),
//    CONSUMED(setOf(valueOf("CANCELLED"))),
//    CANCELLED(setOf(valueOf("ASSIGNED")))
//}
