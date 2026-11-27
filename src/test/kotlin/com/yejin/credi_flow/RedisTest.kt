package com.yejin.credi_flow

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.StringRedisTemplate
import java.time.Duration

@SpringBootTest
class RedisTest @Autowired constructor(
    private val stringRedisTemplate: StringRedisTemplate
) {
    private val testKey = "test:user:1001"

    @AfterEach
    fun tearDown() {
        stringRedisTemplate.delete(testKey)
    }

    @Test
    fun `StringRedisTemplate으로 값을 저장하고 조회 및 삭제한다`() {
        val valueOps = stringRedisTemplate.opsForValue()
        val expectedValue = "hello-redis"

        valueOps.set(testKey, expectedValue, Duration.ofMinutes(5))
        val actualValue = valueOps.get(testKey)
        assertThat(actualValue).isEqualTo(expectedValue)

        //삭제 검증
        assertThat(stringRedisTemplate.delete(testKey)).isTrue()
        assertThat(valueOps.get(testKey)).isNull()
    }
}