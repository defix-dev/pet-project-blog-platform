package ru.defix.blog.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class UserRoleId implements Serializable {
    @Column(name = "user_id")
    private int userId;

    @Column(name = "role_id")
    private int roleId;
}
