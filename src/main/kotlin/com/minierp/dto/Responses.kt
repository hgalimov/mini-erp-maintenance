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
    val status: EquipmentStatus,
    val location: String,
) {
    companion object {
        fun from(e: Equipment) = EquipmentResponse(e.id!!, e.name, e.inventoryNumber, e.status, e.location)
    }
}

data class TechnicianResponse(
    val id: Long,
    val fullName: String,
    val specialization: String,
    val isActive: Boolean,
) {
    companion object {
        fun from(t: Technician) = TechnicianResponse(t.id!!, t.fullName, t.specialization, t.isActive)
    }
}

data class WorkOrderResponse(
    val id: Long,
    val equipmentId: Long,
    val equipmentName: String,
    val technicianId: Long,
    val technicianName: String,
    val description: String,
    val status: WorkOrderStatus,
    val createdAt: LocalDateTime,
    val completedAt: LocalDateTime?,
) {
    companion object {
        fun from(w: WorkOrder) =
            WorkOrderResponse(
                w.id!!,
                w.equipment.id!!,
                w.equipment.name,
                w.technician.id!!,
                w.technician.fullName,
                w.description,
                w.status,
                w.createdAt,
                w.completedAt,
            )
    }
}
