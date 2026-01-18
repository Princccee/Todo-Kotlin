package com.example.todo.service

import com.example.todo.entity.ToDo
import com.example.todo.repository.ToDoRepository
import org.springframework.stereotype.Service
import java.util.NoSuchElementException

@Service
class ToDoService(private val repository: ToDoRepository) {

    fun createTodo(todo: ToDo): ToDo {
        return repository.save(todo)
    }

    fun getAllTodos(): List<ToDo> {
        return repository.findAll()
    }

    fun getTodoById(id: Long): ToDo {
        return repository.findById(id)
            .orElseThrow { NoSuchElementException("ToDo not found with id: $id") }
    }

    fun updateTodo(id: Long, updatedTodo: ToDo): ToDo {
        val existing = repository.findById(id)
            .orElseThrow { NoSuchElementException("ToDo not found with id: $id") }

        val newTodo = existing.copy(
            title = updatedTodo.title,
            description = updatedTodo.description,
            status = updatedTodo.status
        )

        return repository.save(newTodo)
    }

    fun deleteTodo(id: Long) {
        if (!repository.existsById(id)) {
            throw NoSuchElementException("ToDo not found with id: $id")
        }
        repository.deleteById(id)
    }
}
