package com.yejin.credi_flow.report

import com.yejin.credi_flow.common.BaseTimeEntity
import com.yejin.credi_flow.user.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class ReportJob(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    val topic: String,
) : BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: ReportJobStatus = ReportJobStatus.PENDING
        protected set

    @Column(columnDefinition = "text")
    var result: String? = null
        protected set

    fun start() {
        check(status == ReportJobStatus.PENDING) { "PENDING 상태에서만 시작할 수 있습니다." }
        status = ReportJobStatus.PROCESSING
    }

    fun complete(result: String) {
        check(status == ReportJobStatus.PROCESSING) { "PROCESSING 상태에서만 완료할 수 있습니다." }
        this.result = result
        status = ReportJobStatus.COMPLETED
    }
}
