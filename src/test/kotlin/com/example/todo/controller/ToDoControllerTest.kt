package com.example.todo.controller

import com.example.todo.dto.ToDoRequest
import com.example.todo.dto.ToDoResponse
import com.example.todo.entity.TaskStatus
import com.example.todo.service.ToDoService
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class ToDoControllerTest {

    private lateinit var service: ToDoService
    private lateinit var mockMvc: MockMvc
    private val objectMapper = ObjectMapper()

    @BeforeEach
    fun setup() {
        service = mockk()
        val controller = ToDoController(service)
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build()
    }

    @Test
    fun `POST should create todo and return 201`() {
        val request = ToDoRequest(
            title = "Learn Testing",
            description = "MockMvc + MockK",
            status = TaskStatus.PENDING
        )

        val response = ToDoResponse(
            id = 1L,
            title = "Learn Testing",
            description = "MockMvc + MockK",
            status = TaskStatus.PENDING
        )

        every { service.createTodo(any()) } returns response

        mockMvc.perform(
            post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("Learn Testing"))

        verify(exactly = 1) { service.createTodo(any()) }
    }

    @Test
    fun `GET should return paginated list of todos`() {
        val responses = listOf(
            ToDoResponse(1L, "Task 1", "Desc 1", TaskStatus.PENDING),
            ToDoResponse(2L, "Task 2", "Desc 2", TaskStatus.COMPLETED)
        )

        val page = PageImpl(responses, PageRequest.of(0, 10), 2)

        every { service.getAllTodos(0, 10, null) } returns page

        mockMvc.perform(get("/api/todos"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].title").value("Task 1"))
            .andExpect(jsonPath("$.content[1].status").value("COMPLETED"))

        verify(exactly = 1) { service.getAllTodos(0, 10, null) }
    }

    @Test
    fun `GET should filter todos by status`() {
        val responses = listOf(
            ToDoResponse(1L, "Task 1", "Desc 1", TaskStatus.PENDING)
        )

        val page = PageImpl(responses, PageRequest.of(0, 10), 1)

        every {
            service.getAllTodos(0, 10, TaskStatus.PENDING)
        } returns page

        mockMvc.perform(get("/api/todos?status=PENDING"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content[0].status").value("PENDING"))

        verify(exactly = 1) {
            service.getAllTodos(0, 10, TaskStatus.PENDING)
        }
    }

    @Test
    fun `GET by id should return todo`() {
        val response = ToDoResponse(
            id = 1L,
            title = "Task 1",
            description = "Desc 1",
            status = TaskStatus.PENDING
        )

        every { service.getTodoById(1L) } returns response

        mockMvc.perform(get("/api/todos/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("Task 1"))

        verify(exactly = 1) { service.getTodoById(1L) }
    }

    @Test
    fun `DELETE should return 204`() {
        every { service.deleteTodo(1L) } returns Unit

        mockMvc.perform(delete("/api/todos/1"))
            .andExpect(status().isNoContent)

        verify(exactly = 1) { service.deleteTodo(1L) }
    }
}
