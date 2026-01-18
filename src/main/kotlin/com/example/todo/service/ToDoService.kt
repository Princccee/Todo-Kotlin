package com.example.todo.service

import com.example.todo.dto.ToDoRequest
import com.example.todo.dto.ToDoResponse
import com.example.todo.entity.ToDo
import com.example.todo.repository.ToDoRepository
import org.springframework.stereotype.Service
import java.util.NoSuchElementException

@Service
class ToDoService(private val repository: ToDoRepository) {

    fun createTodo(request: ToDoRequest): ToDoResponse {
        val todo = ToDo(
            title = request.title,
            description = request.description,
            status = request.status ?: "PENDING"
        )

        val saved = repository.save(todo)
        return toResponse(saved)
    }

    fun getAllTodos(): List<ToDoResponse> {
        return repository.findAll().map { toResponse(it) }
    }

    fun getTodoById(id: Long): ToDoResponse {
        val todo = repository.findById(id)
            .orElseThrow { NoSuchElementException("ToDo not found with id: $id") }

        return toResponse(todo)
    }

    fun updateTodo(id: Long, request: ToDoRequest): ToDoResponse {
        val existing = repository.findById(id)
            .orElseThrow { NoSuchElementException("ToDo not found with id: $id") }

        val updated = existing.copy(
            title = request.title,
            description = request.description,
            status = request.status ?: existing.status
        )

        return toResponse(repository.save(updated))
    }

    fun deleteTodo(id: Long) {
        if (!repository.existsById(id)) {
            throw NoSuchElementException("ToDo not found with id: $id")
        }
        repository.deleteById(id)
    }

    private fun toResponse(todo: ToDo): ToDoResponse =
        ToDoResponse(
            id = todo.id,
            title = todo.title,
            description = todo.description,
            status = todo.status
        )
}
