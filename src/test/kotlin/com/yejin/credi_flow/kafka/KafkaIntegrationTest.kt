package com.yejin.credi_flow.kafka

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@SpringBootTest
class KafkaIntegrationTest {

    @Autowired
    private lateinit var kafkaProducer: KafkaProducer

    @Autowired
    private lateinit var testConsumer: RealTestKafkaConsumer

    @Test
    fun `실제 Kafka 브로커로 메시지를 발행하고 수신한다`() {
        // given
        val topic = "integration-test-topic"
        val payload = "Real Kafka Test - ${UUID.randomUUID()}"

        // when
        kafkaProducer.send(topic, payload)

        // then
        val messageConsumed = testConsumer.latch.await(10, TimeUnit.SECONDS)
        assertThat(messageConsumed).isTrue()
        assertThat(testConsumer.receivedMessage).isEqualTo(payload)
    }
}

@Component
class RealTestKafkaConsumer {
    val latch = CountDownLatch(1)
    var receivedMessage: String? = null

    @KafkaListener(
        topics = ["integration-test-topic"],
        groupId = "integration-test-group"
    )
    fun listen(message: String) {
        receivedMessage = message
        latch.countDown()
    }
}