package com.minierp.controller

import com.minierp.dto.CreateTechnicianRequest
import com.minierp.dto.TechnicianResponse
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

class TechnicianControllerTest {
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
    fun `should return 201 Created when valid technician is provided`() {
        // Arrange
        val request =
            CreateTechnicianRequest(
                fullName = "John Doe",
                specialization = "Mechanic",
                isActive = true,
            )
        val expectedResponse =
            TechnicianResponse(
                id = 1L,
                fullName = "John Doe",
                specialization = "Mechanic",
                isActive = true,
                statusText = "✅ Активен",
            )

        every { technicianService.create(any()) } returns expectedResponse

        // Act
        val response = controller.createTechnician(request)

        // Assert
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertNotNull(response.body)
        assertEquals("John Doe", response.body?.fullName)
        assertEquals("Mechanic", response.body?.specialization)
        assertEquals(true, response.body?.isActive)
        assertEquals("✅ Активен", response.body?.statusText)
        verify(exactly = 1) { technicianService.create(any()) }
    }

    @Test
    fun `should return list of technicians`() {
        // Arrange
        val expectedList =
            listOf(
                TechnicianResponse(
                    id = 1L,
                    fullName = "John Doe",
                    specialization = "Mechanic",
                    isActive = true,
                    statusText = "✅ Активен",
                ),
                TechnicianResponse(
                    id = 2L,
                    fullName = "Jane Smith",
                    specialization = "Electrician",
                    isActive = false,
                    statusText = "❌ Неактивен",
                ),
            )
        every { technicianService.getAll() } returns expectedList

        // Act
        val response = controller.getAllTechnicians()

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(2, response.body?.size)
        assertEquals("John Doe", response.body?.get(0)?.fullName)
        assertEquals("✅ Активен", response.body?.get(0)?.statusText)
        assertEquals("❌ Неактивен", response.body?.get(1)?.statusText)
    }

    @Test
    fun `should toggle technician active status`() {
        // Arrange
        val expectedResponse =
            TechnicianResponse(
                id = 1L,
                fullName = "John Doe",
                specialization = "Mechanic",
                isActive = false, // toggled from true to false
                statusText = "❌ Неактивен",
            )
        every { technicianService.toggleActiveStatus(1L) } returns expectedResponse

        // Act
        val response = controller.toggleTechnicianStatus(1L)

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(false, response.body?.isActive)
        assertEquals("❌ Неактивен", response.body?.statusText)
        verify(exactly = 1) { technicianService.toggleActiveStatus(1L) }
    }

    @Test
    fun `should throw exception when service fails`() {
        // Arrange
        val request = CreateTechnicianRequest("John", "Mechanic", true)
        every { technicianService.create(any()) } throws IllegalArgumentException("Specialization required")

        // Act & Assert
        val exception =
            assertThrows<IllegalArgumentException> {
                controller.createTechnician(request)
            }
        assertEquals("Specialization required", exception.message)
    }
}
