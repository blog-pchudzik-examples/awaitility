package com.pchudzik.blog.example.awaitility


import spock.lang.Specification

import java.util.concurrent.TimeUnit

import static org.awaitility.Awaitility.await
import static org.hamcrest.Matchers.equalTo

class AwaitilityTest extends Specification {
    final smallQueue = 2
    def messageRepository = new MessageRepository()

    def "consumes all messages if producer is fast"() {
        given:
        final numberOfMessages = 10
        final fastProducer = 10
        final slowConsumer = 100

        and:
        final coordinator = new Coordinator(messageRepository, numberOfMessages, smallQueue, fastProducer, slowConsumer)

        when:
        final consumer = coordinator.startConsumer()
        final producer = coordinator.startProducer()

        then:
        await()
                .atMost(2, TimeUnit.SECONDS)
                .until(
                        { messageRepository.count() },
                        equalTo(numberOfMessages))

        cleanup:
        [consumer, producer, coordinator].each { it.close() }
    }

    def "consumes all messages if producer is slow"() {
        given:
        final numberOfMessages = 10
        final slowProducer = 100
        final fastConsumer = 10

        and:
        final coordinator = new Coordinator(messageRepository, numberOfMessages, smallQueue, slowProducer, fastConsumer)

        when:
        final consumer = coordinator.startConsumer()
        final producer = coordinator.startProducer()

        then:
        await()
                .atMost(2, TimeUnit.SECONDS)
                .until(
                        { messageRepository.count() },
                        equalTo(numberOfMessages))

        cleanup:
        [consumer, producer, coordinator].each { it.close() }
    }
}
