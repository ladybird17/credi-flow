package com.yejin.credi_flow.credit

import org.springframework.data.jpa.repository.JpaRepository

interface CreditRepository : JpaRepository<Credit, Long> {
    fun findByUserId(userId: Long): Credit?
}
