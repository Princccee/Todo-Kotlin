package com.example.todo.repository

import com.example.todo.entity.ToDo
import com.example.todo.entity.TaskStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ToDoRepository : JpaRepository<ToDo, Long> {
    fun findByStatus(status: TaskStatus, pageable: Pageable): Page<ToDo>
}
