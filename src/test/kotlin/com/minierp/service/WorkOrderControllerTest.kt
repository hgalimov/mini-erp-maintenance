package com.minierp.controller

import com.minierp.dto.CreateWorkOrderRequest
import com.minierp.dto.WorkOrderResponse
import com.minierp.service.EquipmentService
import com.minierp.service.TechnicianService
import com.minierp.service.WorkOrderService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class WorkOrderControllerTest {
    private lateinit var equipmentService: EquipmentService
    private lateinit var technicianService: TechnicianService
    private lateinit var workOrderService: WorkOrderService
    private lateinit var controller: ErpController

    @BeforeEach
    fun setUp() {
        equipmentService = mockk()
        technicianService = mockk()
        workOrderService = mockk()
        controller = ErpController(equipmentService, technicianService, workOrderService)
    }

    @Test
    fun `should return 201 Created when valid work order is provided`() {
        // Arrange
        val request =
            CreateWorkOrderRequest(
                equipmentId = 1L,
                technicianId = 2L,
                description = "Replace oil filter",
            )
        val expectedResponse =
            WorkOrderResponse(
                id = 1L,
                equipmentId = 1L,
                equipmentName = "CNC Machine",
                technicianId = 2L,
                technicianName = "John Doe",
                description = "Replace oil filter",
                statusText = "Создан",
                statusClass = "CREATED",
                createdAt = LocalDateTime.now(),
                completedAt = null,
            )

        every { workOrderService.create(any()) } returns expectedResponse

        // Act
        val response = controller.createWorkOrder(request)

        // Assert
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertNotNull(response.body)
        assertEquals(1L, response.body?.id)
        assertEquals("Replace oil filter", response.body?.description)
        assertEquals("Создан", response.body?.statusText)
        assertEquals("CREATED", response.body?.statusClass)
        verify(exactly = 1) { workOrderService.create(any()) }
    }

    @Test
    fun `should return list of work orders`() {
        // Arrange
        val expectedList =
            listOf(
                WorkOrderResponse(
                    id = 1L,
                    equipmentId = 1L,
                    equipmentName = "CNC Machine",
                    technicianId = 2L,
                    technicianName = "John Doe",
                    description = "Replace oil filter",
                    statusText = "Создан",
                    statusClass = "CREATED",
                    createdAt = LocalDateTime.now(),
                    completedAt = null,
                ),
                WorkOrderResponse(
                    id = 2L,
                    equipmentId = 3L,
                    equipmentName = "Press",
                    technicianId = 4L,
                    technicianName = "Jane Smith",
                    description = "Repair motor",
                    statusText = "В работе",
                    statusClass = "IN_PROGRESS",
                    createdAt = LocalDateTime.now(),
                    completedAt = null,
                ),
            )
        every { workOrderService.getAll() } returns expectedList

        // Act
        val response = controller.getAllWorkOrders(null)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(2, response.body?.size)
        assertEquals("Replace oil filter", response.body?.get(0)?.description)
        assertEquals("Создан", response.body?.get(0)?.statusText)
        assertEquals("В работе", response.body?.get(1)?.statusText)
    }

    @Test
    fun `should return filtered list by status`() {
        // Arrange
        val status = "IN_PROGRESS"
        val expectedList =
            listOf(
                WorkOrderResponse(
                    id = 2L,
                    equipmentId = 3L,
                    equipmentName = "Press",
                    technicianId = 4L,
                    technicianName = "Jane Smith",
                    description = "Repair motor",
                    statusText = "В работе",
                    statusClass = "IN_PROGRESS",
                    createdAt = LocalDateTime.now(),
                    completedAt = null,
                ),
            )
        every { workOrderService.getByStatus(status) } returns expectedList

        // Act
        val response = controller.getAllWorkOrders(status)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, response.body?.size)
        assertEquals("В работе", response.body?.get(0)?.statusText)
        assertEquals("IN_PROGRESS", response.body?.get(0)?.statusClass)
    }

    @Test
    fun `should throw exception when service fails`() {
        // Arrange
        val request = CreateWorkOrderRequest(1L, 2L, "Test description")
        every { workOrderService.create(any()) } throws IllegalArgumentException("Equipment not found")

        // Act & Assert
        val exception =
            assertThrows<IllegalArgumentException> {
                controller.createWorkOrder(request)
            }
        assertEquals("Equipment not found", exception.message)
    }
}
