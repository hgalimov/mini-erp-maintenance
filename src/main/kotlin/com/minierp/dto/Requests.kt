package com.minierp.dto
import com.minierp.domain.EquipmentStatus
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateEquipmentRequest(
    @field:NotBlank(message = "Name is required") val name: String,
    @field:NotBlank(message = "Inventory number is required") val inventoryNumber: String,
    @field:NotNull(message = "Status is required") val status: EquipmentStatus,
    @field:NotBlank(message = "Location is required") val location: String,
)

data class CreateTechnicianRequest(
    @field:NotBlank(message = "Full name is required") val fullName: String,
    @field:NotBlank(message = "Specialization is required") val specialization: String,
    val isActive: Boolean = true,
)

data class CreateWorkOrderRequest(
    @field:NotNull(message = "Equipment ID is required") val equipmentId: Long,
    @field:NotNull(message = "Technician ID is required") val technicianId: Long,
    @field:NotBlank(message = "Description is required") val description: String,
)

data class UpdateWorkOrderStatusRequest(
    @field:NotNull(message = "Status is required") val status: com.minierp.domain.WorkOrderStatus,
)
