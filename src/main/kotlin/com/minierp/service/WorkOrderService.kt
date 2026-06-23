package com.minierp.service

import com.minierp.domain.Equipment
import com.minierp.domain.Technician
import com.minierp.domain.WorkOrder
import com.minierp.domain.WorkOrderStatus
import com.minierp.dto.CreateWorkOrderRequest
import com.minierp.dto.UpdateWorkOrderStatusRequest
import com.minierp.dto.WorkOrderResponse
import com.minierp.repository.EquipmentRepository
import com.minierp.repository.TechnicianRepository
import com.minierp.repository.WorkOrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class WorkOrderService(
    private val workOrderRepository: WorkOrderRepository,
    private val equipmentRepository: EquipmentRepository,
    private val technicianRepository: TechnicianRepository,
) {
    fun create(request: CreateWorkOrderRequest): WorkOrderResponse {
        val equipment =
            equipmentRepository
                .findById(request.equipmentId)
                .orElseThrow { throw RuntimeException("Equipment not found with id: ${request.equipmentId}") }

        val technician =
            technicianRepository
                .findById(request.technicianId)
                .orElseThrow { throw RuntimeException("Technician not found with id: ${request.technicianId}") }

        if (!technician.isActive) {
            throw IllegalArgumentException("Cannot assign work order to inactive technician")
        }

        // When creating, change equipment status to MAINTENANCE
        equipmentRepository.save(equipment.copy(status = "MAINTENANCE"))

        val entity =
            WorkOrder(
                equipment = equipment,
                technician = technician,
                description = request.description,
                status = WorkOrderStatus.CREATED, // строка
            )
        val saved = workOrderRepository.save(entity)
        return WorkOrderResponse.from(saved)
    }

    fun getAll(): List<WorkOrderResponse> = workOrderRepository.findAll().map { WorkOrderResponse.from(it) }

    fun getByStatus(status: String): List<WorkOrderResponse> {
        require(WorkOrderStatus.isValid(status)) { "Invalid work order status: $status" }
        return workOrderRepository.findAllByStatus(status).map { WorkOrderResponse.from(it) }
    }

    fun updateStatus(
        id: Long,
        newStatus: String,
    ): WorkOrderResponse {
        require(WorkOrderStatus.isValid(newStatus)) { "Invalid work order status: $newStatus" }

        val workOrder =
            workOrderRepository
                .findById(id)
                .orElseThrow { throw RuntimeException("WorkOrder not found with id: $id") }

        // Prevent changing status of completed/cancelled orders
        if (setOf(WorkOrderStatus.COMPLETED, WorkOrderStatus.CANCELLED).contains(workOrder.status)) {
            throw IllegalArgumentException("Cannot change status of completed/cancelled work order")
        }

        val updated =
            when (newStatus) {
                WorkOrderStatus.COMPLETED -> {
                    // Change equipment back to ACTIVE when work order is completed
                    if (workOrder.equipment.status != "MAINTENANCE") {
                        // Just in case, ensure equipment is in MAINTENANCE before completing
                        equipmentRepository.save(workOrder.equipment.copy(status = "MAINTENANCE"))
                    }
                    workOrder.copy(
                        status = newStatus,
                        completedAt = LocalDateTime.now(),
                    )
                }

                else -> {
                    workOrder.copy(status = newStatus)
                }
            }

        val saved = workOrderRepository.save(updated)
        return WorkOrderResponse.from(saved)
    }
}
