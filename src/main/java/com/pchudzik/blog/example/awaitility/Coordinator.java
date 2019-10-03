package com.pchudzik.blog.example.awaitility;

import lombok.RequiredArgsConstructor;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

@RequiredArgsConstructor
public class Coordinator implements Closeable {
    private final BlockingQueue<String> queue;
    private final MessageRepository messageRepository;
    private final int numberOfMessages;
    private final int producerDelay;
    private final int consumerDelay;
    private final ExecutorService executorService;

    public Coordinator(MessageRepository messageRepository, int numberOfMessages, int queueCapacity, int producerDelay, int consumerDelay) {
        this.executorService = Executors.newFixedThreadPool(2);
        this.messageRepository = messageRepository;
        this.numberOfMessages = numberOfMessages;
        this.queue = new LinkedBlockingDeque<>(queueCapacity);
        this.producerDelay = producerDelay;
        this.consumerDelay = consumerDelay;
    }

    public Consumer startConsumer() {
        final Consumer consumer = new Consumer(queue, messageRepository, consumerDelay);
        executorService.submit(consumer);
        return consumer;
    }

    public Producer startProducer() {
        final Producer producer = new Producer(queue, producerDelay, numberOfMessages);
        executorService.submit(producer);
        return producer;
    }

    @Override
    public void close() throws IOException {
        executorService.shutdown();
    }
}
