package com.pchudzik.blog.example.awaitility;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class Consumer implements Runnable, Closeable {
    private final BlockingQueue<String> queue;
    private final MessageRepository messageRepository;
    private final int delay;
    private boolean running = true;

    @Override
    public void run() {
        while (running) {
            tryToConsumeItem();
        }
    }

    @SneakyThrows
    private void tryToConsumeItem() {
        final String item = queue.poll(50, TimeUnit.MILLISECONDS);
        if (item != null) {
            messageRepository.save(item);
            log.info("Received message {}", item);
        }
        Thread.sleep(delay);
    }

    @Override
    public void close() throws IOException {
        running = false;
    }
}
