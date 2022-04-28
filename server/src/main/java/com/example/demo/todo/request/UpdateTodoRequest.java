package com.example.demo.todo.request;

import org.hibernate.sql.Update;

public class UpdateTodoRequest {
    private Long todoId;
    private String text;

    public UpdateTodoRequest() {}

    public UpdateTodoRequest(Long todoId, String text) {
        this.todoId = todoId;
        this.text = text;
    }

    public Long getTodoId() {
        return todoId;
    }

    public void setTodoId(Long todoId) {
        this.todoId = todoId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "UpdateTodoRequest{" +
                "todoId=" + todoId +
                ", text='" + text + '\'' +
                '}';
    }
}
