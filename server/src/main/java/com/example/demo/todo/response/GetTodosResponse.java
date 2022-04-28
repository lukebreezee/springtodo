package com.example.demo.todo.response;

import com.example.demo.todo.Todo;

import java.util.List;

public class GetTodosResponse {
    private List<Todo> todos;

    public GetTodosResponse(List<Todo> todos) {
        this.todos = todos;
    }

    public List<Todo> getTodos() {
        return todos;
    }

    public void setTodos(List<Todo> todos) {
        this.todos = todos;
    }

    @Override
    public String toString() {
        return "getTodosResponse{" +
                "todos=" + todos +
                '}';
    }
}
