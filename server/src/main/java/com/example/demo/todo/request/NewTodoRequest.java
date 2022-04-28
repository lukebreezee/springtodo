package com.example.demo.todo.request;

public class NewTodoRequest {
    private String text;

    public NewTodoRequest() {}

    public NewTodoRequest(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "NewTodoRequest{" +
                "text='" + text + '\'' +
                '}';
    }
}
