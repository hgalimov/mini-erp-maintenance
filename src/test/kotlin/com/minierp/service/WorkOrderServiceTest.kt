package com.minierp.service

import com.minierp.domain.Equipment
import com.minierp.domain.EquipmentStatus
import com.minierp.domain.Technician
import com.minierp.domain.WorkOrder
import com.minierp.domain.WorkOrderStatus
import com.minierp.dto.CreateWorkOrderRequest
import com.minierp.repository.EquipmentRepository
import com.minierp.repository.TechnicianRepository
import com.minierp.repository.WorkOrderRepository
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime
import java.util.Optional

@ExtendWith(MockKExtension::class)
class WorkOrderServiceTest {
    private val workOrderRepository = mockk<WorkOrderRepository>()
    private val equipmentRepository = mockk<EquipmentRepository>()
    private val technicianRepository = mockk<TechnicianRepository>()

    private val service = WorkOrderService(workOrderRepository, equipmentRepository, technicianRepository)

    @Test
    fun `should create work order and update equipment status`() {
        val equipment = Equipment(1L, "CNC", "INV-001", EquipmentStatus.ACTIVE, "Shop")
        val technician = Technician(1L, "John", "Mechanic", true)
        val request = CreateWorkOrderRequest(1L, 1L, "Fix oil")

        every { equipmentRepository.findById(1L) } returns Optional.of(equipment)
        every { technicianRepository.findById(1L) } returns Optional.of(technician)
        every { equipmentRepository.save(any()) } returns equipment.copy(status = EquipmentStatus.MAINTENANCE)

        every { workOrderRepository.save(any()) } answers {
            val workOrder = firstArg<WorkOrder>()
            workOrder.copy(id = 100L, createdAt = LocalDateTime.now())
        }

        val result = service.create(request)

        assertEquals(WorkOrderStatus.CREATED, result.status)
        assertEquals("CNC", result.equipmentName)
        assertEquals(100L, result.id)
        verify(exactly = 1) { equipmentRepository.save(match { it.status == EquipmentStatus.MAINTENANCE }) }
    }

    @Test
    fun `should fail when technician is inactive`() {
        val equipment = Equipment(1L, "CNC", "INV-001", EquipmentStatus.ACTIVE, "Shop")
        val technician = Technician(1L, "John", "Mechanic", false)
        val request = CreateWorkOrderRequest(1L, 1L, "Fix")

        every {
            equipmentRepository.findById(1L)
        } returns Optional.of(equipment)
        every {
            technicianRepository.findById(1L)
        } returns Optional.of(technician)

        val exception =
            assertThrows<IllegalArgumentException> {
                service.create(request)
            }
        assertEquals("Cannot assign inactive technician", exception.message)
    }
}
