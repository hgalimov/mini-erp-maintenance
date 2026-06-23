package com.minierp.dto

import com.minierp.domain.EquipmentStatus
import com.minierp.domain.WorkOrderStatus
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateEquipmentRequest(
    @field:NotBlank(message = "Название не может быть пустым")
    val name: String,
    @field:NotBlank(message = "Инвентарный номер не может быть пустым")
    val inventoryNumber: String,
    @field:NotBlank(message = "Статус обязателен")
    val status: String,
    @field:NotBlank(message = "Локация не может быть пустой")
    val location: String,
) {
    init {
        require(EquipmentStatus.isValid(status)) { "Недопустимый статус оборудования: $status" }
    }
}

data class CreateTechnicianRequest(
    @field:NotBlank(message = "ФИО не может быть пустым")
    val fullName: String,
    @field:NotBlank(message = "Специализация не может быть пустой")
    val specialization: String,
    val isActive: Boolean = true,
)

data class CreateWorkOrderRequest(
    val equipmentId: Long,
    val technicianId: Long,
    @field:NotBlank(message = "Описание не может быть пустым")
    val description: String,
)

data class UpdateWorkOrderStatusRequest(
    @field:NotBlank(message = "Статус обязателен")
    val status: String,
) {
    init {
        require(WorkOrderStatus.isValid(status)) { "Недопустимый статус наряд-заказа: $status" }
    }
}
