package com.example.todo.service

import com.example.todo.dto.ToDoRequest
import com.example.todo.entity.ToDo
import com.example.todo.repository.ToDoRepository
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional

class ToDoServiceTest {

    private lateinit var repository: ToDoRepository
    private lateinit var service: ToDoService

    @BeforeEach
    fun setup() {
        repository = mockk()
        service = ToDoService(repository)
    }

    @Test
    fun `should create todo successfully`() {
        // Given
        val request = ToDoRequest(
            title = "Write tests",
            description = "Using JUnit and MockK",
            status = "PENDING"
        )

        val savedTodo = ToDo(
            id = 1L,
            title = "Write tests",
            description = "Using JUnit and MockK",
            status = "PENDING"
        )

        every { repository.save(any()) } returns savedTodo

        // When
        val response = service.createTodo(request)

        // Then
        assertEquals(1L, response.id)
        assertEquals("Write tests", response.title)
        assertEquals("PENDING", response.status)

        verify(exactly = 1) { repository.save(any()) }
    }

    @Test
    fun `should return all todos`() {
        // Given
        val todos = listOf(
            ToDo(1L, "Task 1", "Desc 1", "PENDING"),
            ToDo(2L, "Task 2", "Desc 2", "COMPLETED")
        )

        every { repository.findAll() } returns todos

        // When
        val result = service.getAllTodos()

        // Then
        assertEquals(2, result.size)
        assertEquals("Task 1", result[0].title)
        verify(exactly = 1) { repository.findAll() }
    }

    @Test
    fun `should throw exception when todo not found`() {
        // Given
        every { repository.findById(99L) } returns Optional.empty()

        // When & Then
        val exception = assertThrows(NoSuchElementException::class.java) {
            service.getTodoById(99L)
        }

        assertTrue(exception.message!!.contains("ToDo not found"))
        verify(exactly = 1) { repository.findById(99L) }
    }
}
