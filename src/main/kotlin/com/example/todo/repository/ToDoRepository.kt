package com.example.todo.repository

import com.example.todo.entity.ToDo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ToDoRepository : JpaRepository<ToDo, Long>{
    fun findByStatus(status: String, pageable: org.springframework.data.domain.Pageable)
            : org.springframework.data.domain.Page<ToDo>
}
