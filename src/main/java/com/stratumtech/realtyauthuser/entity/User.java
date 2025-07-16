package com.stratumtech.realtyauthuser.entity;

import java.util.UUID;
import java.time.Instant;
import java.sql.Timestamp;

import lombok.Data;

import jakarta.persistence.*;

@Data
@MappedSuperclass
public abstract class User {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "telegram_tag", nullable = false)
    private String telegramTag;

    @Column(name = "prefer_channel", nullable = false)
    private String preferChannel;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @Column(name = "is_blocked", nullable = false)
    private Boolean isBlocked = false;

    @PrePersist
    protected void onCreate() {
        Timestamp now = Timestamp.from(Instant.now());
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public abstract UUID getId();
    public abstract Role getRole();
    public abstract Region getRegion();
}