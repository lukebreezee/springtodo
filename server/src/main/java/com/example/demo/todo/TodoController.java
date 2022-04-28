package com.example.demo.todo;

import com.example.demo.payload.response.GenericResponse;
import com.example.demo.todo.request.NewTodoRequest;
import com.example.demo.todo.request.UpdateTodoRequest;
import com.example.demo.todo.response.GetTodosResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class TodoController {
    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("todo")
    public ResponseEntity<GetTodosResponse> getTodos(@RequestHeader Map<String, String> headers) {
        try {
            System.out.println(headers.get("id"));
            List<Todo> todos = todoService.getTodos(Long.parseLong(headers.get("id")));
            return new ResponseEntity<>(
                    new GetTodosResponse(todos),
                    HttpStatus.ACCEPTED
            );
        } catch (Exception e) {
            List<Todo> emptyList = new ArrayList<>();
            return new ResponseEntity<>(
                    new GetTodosResponse(emptyList),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @PostMapping(path = "todo",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> newTodo(
                @RequestBody NewTodoRequest request,
                @RequestHeader Map<String, String> headers
            ) {
        try {
            String uid = headers.get("id");
            Date newDate = new Date(System.currentTimeMillis());
            Todo todo = new Todo(request.getText(), newDate, Long.parseLong(uid));
            todoService.saveTodo(todo);
            return new ResponseEntity<>(
                    new GenericResponse(true, "New todo created"),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new GenericResponse(false, "An error has occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PutMapping("todo")
    public ResponseEntity<GenericResponse> updateTodo(@RequestBody UpdateTodoRequest request) {
        try {
            todoService.updateTodoText(request.getTodoId(), request.getText());
            return new ResponseEntity<>(
                    new GenericResponse(true, "Todo updated"),
                    HttpStatus.ACCEPTED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new GenericResponse(false, "An error has occurred"),
                    HttpStatus.ACCEPTED
            );
        }
    }

    @DeleteMapping("todo")
    public ResponseEntity<GenericResponse> deleteTodo(@RequestHeader Map<String, String> headers) {
        try {
            todoService.deleteTodo(Long.parseLong(headers.get("todoid")));
            return new ResponseEntity<>(
                    new GenericResponse(true, "Todo deleted"),
                    HttpStatus.ACCEPTED
            );
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(
                    new GenericResponse(false, "An error has occurred"),
                    HttpStatus.ACCEPTED
            );
        }
    }

}
