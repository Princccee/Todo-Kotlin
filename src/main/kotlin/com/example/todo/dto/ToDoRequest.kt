package com.example.todo.dto

import com.example.todo.entity.TaskStatus
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ToDoRequest(

    @field:NotBlank(message = "Title cannot be empty")
    @field:Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    val title: String,

    @field:Size(max = 255, message = "Description cannot exceed 255 characters")
    val description: String? = null,

    val status: TaskStatus? = TaskStatus.PENDING
)
