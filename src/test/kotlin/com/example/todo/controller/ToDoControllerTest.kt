package com.example.todo.controller

import com.example.todo.dto.ToDoRequest
import com.example.todo.dto.ToDoResponse
import com.example.todo.service.ToDoService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class ToDoControllerTest {

    private lateinit var service: ToDoService
    private lateinit var mockMvc: MockMvc
    private val objectMapper = jacksonObjectMapper()

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
            status = "PENDING"
        )

        val response = ToDoResponse(
            id = 1L,
            title = "Learn Testing",
            description = "MockMvc + MockK",
            status = "PENDING"
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
    fun `GET should return list of todos`() {
        val responses = listOf(
            ToDoResponse(1L, "Task 1", "Desc 1", "PENDING"),
            ToDoResponse(2L, "Task 2", "Desc 2", "COMPLETED")
        )

        every { service.getAllTodos() } returns responses

        mockMvc.perform(get("/api/todos"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].title").value("Task 1"))
            .andExpect(jsonPath("$[1].status").value("COMPLETED"))

        verify(exactly = 1) { service.getAllTodos() }
    }
}
