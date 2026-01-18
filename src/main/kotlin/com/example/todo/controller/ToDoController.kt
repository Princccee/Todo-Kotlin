package com.example.todo.controller

import com.example.todo.dto.ToDoRequest
import com.example.todo.dto.ToDoResponse
import com.example.todo.service.ToDoService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/todos")
class ToDoController(private val service: ToDoService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createTodo(@Valid @RequestBody request: ToDoRequest): ToDoResponse {
        return service.createTodo(request)
    }

    @GetMapping
    fun getAllTodos(): List<ToDoResponse> {
        return service.getAllTodos()
    }

    @GetMapping("/{id}")
    fun getTodoById(@PathVariable id: Long): ToDoResponse {
        return service.getTodoById(id)
    }

    @PutMapping("/{id}")
    fun updateTodo(
        @PathVariable id: Long,
        @Valid @RequestBody request: ToDoRequest
    ): ToDoResponse {
        return service.updateTodo(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTodo(@PathVariable id: Long) {
        service.deleteTodo(id)
    }
}
