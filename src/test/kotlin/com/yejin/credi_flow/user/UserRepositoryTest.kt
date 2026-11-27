package com.yejin.credi_flow.user

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@SpringBootTest
@Transactional
class UserRepositoryTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val queryFactory: JPAQueryFactory,
    private val em: EntityManager,
) {
    private fun newUser() = User(email = "${UUID.randomUUID()}@test.com", name = "tester")

    @Test
    fun `저장한 User를 다시 조회한다`() {
        val saved = userRepository.save(newUser())
        em.flush()
        em.clear()

        val found = userRepository.findByEmail(saved.email)

        assertThat(found).isNotNull
        assertThat(found!!.id).isEqualTo(saved.id)
        assertThat(found.name).isEqualTo("tester")
    }

    @Test
    fun `저장하면 생성 시각이 자동으로 들어간다`() {
        val saved = userRepository.save(newUser())

        assertThat(saved.createdAt).isNotNull()
        assertThat(saved.updatedAt).isNotNull()
    }

    @Test
    fun `JPAQueryFactory로 User를 조회한다`() {
        val saved = userRepository.save(newUser())
        em.flush()
        em.clear()

        val user = QUser.user
        val found = queryFactory
            .selectFrom(user)
            .where(user.email.eq(saved.email))
            .fetchOne()

        assertThat(found?.id).isEqualTo(saved.id)
    }
}
