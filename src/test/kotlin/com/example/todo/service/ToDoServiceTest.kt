package com.example.todo.service

import com.example.todo.dto.ToDoRequest
import com.example.todo.entity.TaskStatus
import com.example.todo.entity.ToDo
import com.example.todo.repository.ToDoRepository
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.util.NoSuchElementException
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
            status = TaskStatus.PENDING
        )

        val savedTodo = ToDo(
            id = 1L,
            title = "Write tests",
            description = "Using JUnit and MockK",
            status = TaskStatus.PENDING
        )

        every { repository.save(any()) } returns savedTodo

        // When
        val response = service.createTodo(request)

        // Then
        assertEquals(1L, response.id)
        assertEquals("Write tests", response.title)
        assertEquals(TaskStatus.PENDING, response.status)

        verify(exactly = 1) { repository.save(any()) }
    }

    @Test
    fun `should return paginated todos`() {
        // Given
        val todos = listOf(
            ToDo(1L, "Task 1", "Desc 1", TaskStatus.PENDING),
            ToDo(2L, "Task 2", "Desc 2", TaskStatus.COMPLETED)
        )

        val page = PageImpl(todos, PageRequest.of(0, 10), 2)

        every { repository.findAll(any<Pageable>()) } returns page

        // When
        val result = service.getAllTodos(0, 10, null)

        // Then
        assertEquals(2, result.content.size)
        assertEquals("Task 1", result.content[0].title)
        verify(exactly = 1) { repository.findAll(any<Pageable>()) }
    }

    @Test
    fun `should filter todos by status`() {
        // Given
        val todos = listOf(
            ToDo(1L, "Task 1", "Desc 1", TaskStatus.PENDING)
        )

        val page = PageImpl(todos, PageRequest.of(0, 10), 1)

        every {
            repository.findByStatus(TaskStatus.PENDING, any<Pageable>())
        } returns page

        // When
        val result = service.getAllTodos(0, 10, TaskStatus.PENDING)

        // Then
        assertEquals(1, result.content.size)
        assertEquals(TaskStatus.PENDING, result.content[0].status)

        verify(exactly = 1) {
            repository.findByStatus(TaskStatus.PENDING, any<Pageable>())
        }
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

    @Test
    fun `should update todo successfully`() {
        // Given
        val existing = ToDo(
            id = 1L,
            title = "Old Title",
            description = "Old Desc",
            status = TaskStatus.PENDING
        )

        val request = ToDoRequest(
            title = "New Title",
            description = "New Desc",
            status = TaskStatus.COMPLETED
        )

        val updated = existing.copy(
            title = request.title,
            description = request.description,
            status = request.status!!
        )

        every { repository.findById(1L) } returns Optional.of(existing)
        every { repository.save(any()) } returns updated

        // When
        val response = service.updateTodo(1L, request)

        // Then
        assertEquals("New Title", response.title)
        assertEquals(TaskStatus.COMPLETED, response.status)

        verify(exactly = 1) { repository.findById(1L) }
        verify(exactly = 1) { repository.save(any()) }
    }

    @Test
    fun `should delete todo successfully`() {
        every { repository.existsById(1L) } returns true
        every { repository.deleteById(1L) } returns Unit

        service.deleteTodo(1L)

        verify(exactly = 1) { repository.existsById(1L) }
        verify(exactly = 1) { repository.deleteById(1L) }
    }
}
