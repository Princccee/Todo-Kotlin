package com.example.todo.dto

data class ToDoResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val status: String
)
