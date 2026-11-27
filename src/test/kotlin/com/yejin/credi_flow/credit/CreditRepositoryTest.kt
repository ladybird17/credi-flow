package com.yejin.credi_flow.credit

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
class CreditRepositoryTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val creditRepository: CreditRepository,
    private val em: EntityManager,
) {
    private fun newUser() = userRepository.save(User(email = "${UUID.randomUUID()}@test.com", name = "tester"))

    @Test
    fun `충전하면 잔액이 늘어난다`() {
        val user = newUser()
        val credit = creditRepository.save(Credit(user))

        credit.charge(1000)
        em.flush()
        em.clear()

        val found = creditRepository.findByUserId(user.id!!)
        assertThat(found!!.balance).isEqualTo(1000)
    }

    @Test
    fun `0 이하 금액은 충전할 수 없다`() {
        val credit = Credit(newUser())

        assertThatThrownBy { credit.charge(0) }.isInstanceOf(IllegalArgumentException::class.java)
        assertThatThrownBy { credit.charge(-100) }.isInstanceOf(IllegalArgumentException::class.java)
        assertThat(credit.balance).isZero()
    }
}
