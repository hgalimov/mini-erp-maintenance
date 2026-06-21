package com.minierp.service
import com.minierp.domain.EquipmentStatus
import com.minierp.domain.WorkOrderStatus
import com.minierp.dto.CreateWorkOrderRequest
import com.minierp.dto.WorkOrderResponse
import com.minierp.repository.EquipmentRepository
import com.minierp.repository.TechnicianRepository
import com.minierp.repository.WorkOrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class WorkOrderService(
    private val workOrderRepo: WorkOrderRepository,
    private val equipRepo: EquipmentRepository,
    private val techRepo: TechnicianRepository,
) {
    @Transactional(readOnly = true)
    fun getAll() = workOrderRepo.findAll().map { WorkOrderResponse.from(it) }

    @Transactional(readOnly = true)
    fun getByStatus(status: WorkOrderStatus) = workOrderRepo.findAllByStatus(status).map { WorkOrderResponse.from(it) }

    @Transactional
    fun create(req: CreateWorkOrderRequest): WorkOrderResponse {
        val equip = equipRepo.findById(req.equipmentId).orElseThrow { IllegalArgumentException("Equipment not found") }
        val tech = techRepo.findById(req.technicianId).orElseThrow { IllegalArgumentException("Technician not found") }
        if (!tech.isActive) throw IllegalArgumentException("Cannot assign inactive technician")

        equipRepo.save(equip.copy(status = EquipmentStatus.MAINTENANCE))
        val order =
            workOrderRepo.save(
                com.minierp.domain.WorkOrder(
                    equipment = equip,
                    technician = tech,
                    description = req.description,
                    status = WorkOrderStatus.CREATED,
                ),
            )
        return WorkOrderResponse.from(order)
    }

    @Transactional
    fun updateStatus(
        id: Long,
        newStatus: WorkOrderStatus,
    ): WorkOrderResponse {
        val order = workOrderRepo.findById(id).orElseThrow { IllegalArgumentException("Work order not found") }
        if (order.status == WorkOrderStatus.COMPLETED || order.status == WorkOrderStatus.CANCELLED) {
            throw IllegalArgumentException("Cannot change completed/cancelled order")
        }

        val finalStatus =
            if (newStatus == WorkOrderStatus.COMPLETED) {
                equipRepo.save(order.equipment.copy(status = EquipmentStatus.ACTIVE))
                WorkOrderStatus.COMPLETED
            } else {
                newStatus
            }

        return WorkOrderResponse.from(
            workOrderRepo.save(
                order.copy(
                    status = finalStatus,
                    completedAt =
                        if (finalStatus ==
                            WorkOrderStatus.COMPLETED
                        ) {
                            LocalDateTime.now()
                        } else {
                            order.completedAt
                        },
                ),
            ),
        )
    }
}
