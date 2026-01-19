package com.example.todo.dto

import com.example.todo.entity.TaskStatus

data class ToDoResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val status: TaskStatus
)
