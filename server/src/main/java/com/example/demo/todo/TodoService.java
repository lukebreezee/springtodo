package com.example.demo.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> getTodos(Long id) {
        return todoRepository.findAllByUid(id);
    }

    public Boolean saveTodo(Todo todo) {
        try {
            todoRepository.save(todo);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean updateTodoText(Long id, String text) {
        Optional<Todo> todoToUpdate = todoRepository.findById(id);
        if (todoToUpdate == null) {
            return false;
        }
        todoToUpdate.get().setText(text);
        todoRepository.save(todoToUpdate.get());
        return true;
    }

    public Boolean deleteTodo(Long id) {
        try {
            todoRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
