package com.yejin.credi_flow.report

import com.yejin.credi_flow.user.User
import com.yejin.credi_flow.user.UserRepository
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@SpringBootTest
@Transactional
class ReportJobRepositoryTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val reportJobRepository: ReportJobRepository,
    private val em: EntityManager,
) {
    private fun newUser() = userRepository.save(User(email = "${UUID.randomUUID()}@test.com", name = "tester"))

    private fun statusInDb(id: Long): Any =
        em.createNativeQuery("select status from report_job where id = :id")
            .setParameter("id", id)
            .singleResult

    @Test
    fun `상태가 DB에 문자열로 저장된다`() {
        val job = reportJobRepository.save(ReportJob(newUser(), "3분기 매출 분석"))
        em.flush()
        assertThat(statusInDb(job.id!!)).isEqualTo("PENDING")

        job.start()
        em.flush()
        assertThat(statusInDb(job.id!!)).isEqualTo("PROCESSING")

        job.complete("dummy report")
        em.flush()
        assertThat(statusInDb(job.id!!)).isEqualTo("COMPLETED")
    }

    @Test
    fun `PENDING이 아니면 시작할 수 없다`() {
        val job = ReportJob(newUser(), "3분기 매출 분석")
        job.start()

        assertThatThrownBy { job.start() }.isInstanceOf(IllegalStateException::class.java)
    }
}
