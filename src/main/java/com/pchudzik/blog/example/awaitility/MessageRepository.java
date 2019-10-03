package com.pchudzik.blog.example.awaitility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageRepository {
    private final List<String> repo = new ArrayList<>();

    public void save(String message) {
        repo.add(message);
    }

    public List<String> findAll() {
        return Collections.unmodifiableList(repo);
    }

    public int count() {
        return repo .size();
    }
}
