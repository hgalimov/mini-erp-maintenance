package com.minierp.controller

import com.minierp.domain.EquipmentStatus
import com.minierp.dto.CreateEquipmentRequest
import com.minierp.dto.EquipmentResponse
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

class EquipmentControllerTest {
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
    fun `should return 201 Created when valid equipment is provided`() {
        // Arrange
        val request =
            CreateEquipmentRequest(
                name = "Lathe",
                inventoryNumber = "INV-002",
                status = EquipmentStatus.ACTIVE,
                location = "Building A",
            )
        val expectedResponse =
            EquipmentResponse(
                id = 1L,
                name = "Lathe",
                inventoryNumber = "INV-002",
//                status = EquipmentStatus.ACTIVE,
//                statusText = "Активно",
//                statusClass = "ACTIVE",
                location = "Building A",
            )

        every { equipmentService.create(any()) } returns expectedResponse

        // Act
        val response = controller.createEquipment(request)

        // Assert
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertNotNull(response.body)
        assertEquals("Lathe", response.body?.name)
        assertEquals("INV-002", response.body?.inventoryNumber)
//        assertEquals("Активно", response.body?.statusText)
//        assertEquals("ACTIVE", response.body?.statusClass)
        verify(exactly = 1) { equipmentService.create(any()) }
    }

    @Test
    fun `should return list of equipment`() {
        // Arrange
        val expectedList =
            listOf(
                EquipmentResponse(
                    id = 1L,
                    name = "Lathe",
                    inventoryNumber = "INV-001",
//                    status = EquipmentStatus.ACTIVE,
//                    statusText = "Активно",
//                    statusClass = "ACTIVE",
                    location = "Building A",
                ),
                EquipmentResponse(
                    id = 2L,
                    name = "CNC",
                    inventoryNumber = "INV-002",
//                    status = EquipmentStatus.BROKEN,
//                    statusText = "Сломано",
//                    statusClass = "BROKEN",
                    location = "Building B",
                ),
            )
        every { equipmentService.getAll() } returns expectedList

        // Act
        val response = controller.getAllEquipment()

        // Assert
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(2, response.body?.size)
        assertEquals("Lathe", response.body?.get(0)?.name)
//        assertEquals("Активно", response.body?.get(0)?.statusText)
//        assertEquals("ACTIVE", response.body?.get(0)?.statusClass)
    }

    @Test
    fun `should throw exception when service fails`() {
        // Arrange
        val request = CreateEquipmentRequest("Lathe", "INV-DUP", EquipmentStatus.ACTIVE, "A")
        every { equipmentService.create(any()) } throws IllegalArgumentException("Inventory number exists")

        // Act & Assert
        val exception =
            assertThrows<IllegalArgumentException> {
                controller.createEquipment(request)
            }
        assertEquals("Inventory number exists", exception.message)
    }
}
