package com.example.todo.controller

import com.example.todo.entity.ToDo
import com.example.todo.service.ToDoService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/todos")
class ToDoController(private val service: ToDoService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createTodo(@RequestBody todo: ToDo): ToDo {
        return service.createTodo(todo)
    }

    @GetMapping
    fun getAllTodos(): List<ToDo> {
        return service.getAllTodos()
    }

    @GetMapping("/{id}")
    fun getTodoById(@PathVariable id: Long): ToDo {
        return service.getTodoById(id)
    }

    @PutMapping("/{id}")
    fun updateTodo(
        @PathVariable id: Long,
        @RequestBody todo: ToDo
    ): ToDo {
        return service.updateTodo(id, todo)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTodo(@PathVariable id: Long) {
        service.deleteTodo(id)
    }
}
