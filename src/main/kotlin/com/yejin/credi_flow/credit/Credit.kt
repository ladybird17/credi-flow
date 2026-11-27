package com.yejin.credi_flow.credit

import com.yejin.credi_flow.common.BaseTimeEntity
import com.yejin.credi_flow.user.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne

@Entity
class Credit(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    val user: User,

    balance: Long = 0,
) : BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(nullable = false)
    var balance: Long = balance
        protected set

    fun charge(amount: Long) {
        require(amount > 0) { "충전 금액은 0보다 커야 합니다." }
        balance += amount
    }
}
