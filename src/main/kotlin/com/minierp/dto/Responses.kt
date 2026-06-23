package com.minierp.dto

import com.minierp.domain.Equipment
import com.minierp.domain.EquipmentStatus
import com.minierp.domain.Technician
import com.minierp.domain.WorkOrder
import com.minierp.domain.WorkOrderStatus
import java.time.LocalDateTime

data class EquipmentResponse(
    val id: Long,
    val name: String,
    val inventoryNumber: String,
    val statusText: String,
    val statusClass: String,
    val location: String,
) {
    companion object {
        fun from(entity: Equipment): EquipmentResponse {
            val (text, cssClass) =
                when (entity.status) {
                    EquipmentStatus.ACTIVE -> "Активно" to "ACTIVE"
                    EquipmentStatus.BROKEN -> "Сломано" to "BROKEN"
                    EquipmentStatus.MAINTENANCE -> "На обслуживании" to "MAINTENANCE"
                    EquipmentStatus.DECOMMISSIONED -> "Выведено" to "DECOMMISSIONED"
                    else -> "Неизвестно" to "UNKNOWN"
                }

            return EquipmentResponse(
                id = entity.id ?: throw IllegalStateException("Equipment id is null"),
                name = entity.name,
                inventoryNumber = entity.inventoryNumber,
                statusText = text,
                statusClass = cssClass,
                location = entity.location,
            )
        }
    }
}

data class TechnicianResponse(
    val id: Long,
    val fullName: String,
    val specialization: String,
    val isActive: Boolean,
    val statusText: String,
) {
    companion object {
        fun from(t: Technician): TechnicianResponse {
            val text = if (t.isActive) "✅ Активен" else "❌ Неактивен"
            return TechnicianResponse(
                id = t.id ?: throw IllegalStateException("Technician id is null"),
                fullName = t.fullName,
                specialization = t.specialization,
                isActive = t.isActive,
                statusText = text,
            )
        }
    }
}

data class WorkOrderResponse(
    val id: Long,
    val equipmentId: Long,
    val equipmentName: String,
    val technicianId: Long,
    val technicianName: String,
    val description: String,
    val statusText: String,
    val statusClass: String,
    val createdAt: LocalDateTime,
    val completedAt: LocalDateTime?,
) {
    companion object {
        fun from(w: WorkOrder): WorkOrderResponse {
            val (text, cssClass) =
                when (w.status) {
                    WorkOrderStatus.CREATED -> "Создан" to "CREATED"
                    WorkOrderStatus.IN_PROGRESS -> "В работе" to "IN_PROGRESS"
                    WorkOrderStatus.COMPLETED -> "Завершён" to "COMPLETED"
                    WorkOrderStatus.CANCELLED -> "Отменён" to "CANCELLED"
                    else -> "Неизвестно" to "UNKNOWN"
                }

            return WorkOrderResponse(
                id = w.id ?: throw IllegalStateException("WorkOrder id is null"),
                equipmentId = w.equipment.id ?: throw IllegalStateException("Equipment id is null"),
                equipmentName = w.equipment.name,
                technicianId = w.technician.id ?: throw IllegalStateException("Technician id is null"),
                technicianName = w.technician.fullName,
                description = w.description,
                statusText = text,
                statusClass = cssClass,
                createdAt = w.createdAt,
                completedAt = w.completedAt,
            )
        }
    }
}
