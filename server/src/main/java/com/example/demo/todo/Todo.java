package com.example.demo.todo;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "todos")
public class Todo {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    private String text;
    private Date updatedAt;
    private Long uid;

    public Todo() {}

    public Todo(String text, Date updatedAt, Long uid) {
        this.text = text;
        this.updatedAt = updatedAt;
        this.uid = uid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", updatedAt=" + updatedAt +
                ", uid=" + uid +
                '}';
    }
}
