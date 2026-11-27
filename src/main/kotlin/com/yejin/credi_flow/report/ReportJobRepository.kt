package com.yejin.credi_flow.report

import org.springframework.data.jpa.repository.JpaRepository

interface ReportJobRepository : JpaRepository<ReportJob, Long> {
    fun findAllByUserId(userId: Long): List<ReportJob>
}
