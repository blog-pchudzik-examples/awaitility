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
public class Producer implements Runnable, Closeable {
    private final BlockingQueue<String> queue;
    private final int delay;
    private final int items;

    private boolean running = true;

    @Override
    public void run() {
        for (int i = 0; i < items && running; i++) {
            sendHeavyItem(i);
        }
    }

    @SneakyThrows
    private void sendHeavyItem(int i) {
        final String message = "hello " + i;
        boolean result;
        do {
            result = queue.offer(message, 50, TimeUnit.MILLISECONDS);
            Thread.sleep(delay);
        } while (!result);

        log.info("Send message {}", message);
    }

    @Override
    public void close() throws IOException {
        running = false;
    }
}
