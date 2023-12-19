package ru.lozovoi.entity;

import java.util.concurrent.atomic.AtomicLong;

public class BaseEntity {

    private static final AtomicLong ID = new AtomicLong(10);

    private final Long id;

    public BaseEntity() {
        this.id = ID.getAndIncrement();
    }

    public Long getId() {
        return id;
    }
}
