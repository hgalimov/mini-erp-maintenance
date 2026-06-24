package com.minierp.service

import com.minierp.domain.Equipment
import com.minierp.domain.EquipmentStatus
import com.minierp.dto.CreateEquipmentRequest
import com.minierp.dto.EquipmentResponse
import com.minierp.repository.EquipmentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class EquipmentService(
    private val equipmentRepository: EquipmentRepository,
) {
    fun create(request: CreateEquipmentRequest): EquipmentResponse {
        // Проверяем уникальность инвентарного номера
        if (equipmentRepository.existsByInventoryNumber(request.inventoryNumber)) {
            throw IllegalArgumentException("Оборудование с инвентарным номером ${request.inventoryNumber} уже существует")
        }

        val entity =
            Equipment(
                name = request.name,
                inventoryNumber = request.inventoryNumber,
                status = request.status,
                location = request.location,
            )
        val saved = equipmentRepository.save(entity)
        return EquipmentResponse.from(saved)
    }

    fun getAll(): List<EquipmentResponse> {
        val all = equipmentRepository.findAll()
        return all.map { EquipmentResponse.from(it) }
    }

    fun getById(id: Long): EquipmentResponse {
        val equipment =
            equipmentRepository
                .findById(id)
                .orElseThrow { throw RuntimeException("Equipment not found with id: $id") }
        return EquipmentResponse.from(equipment)
    }

    fun updateStatus(
        id: Long,
        newStatus: String,
    ): EquipmentResponse {
        require(EquipmentStatus.isValid(newStatus)) { "Invalid equipment status: $newStatus" }

        val equipment =
            equipmentRepository
                .findById(id)
                .orElseThrow { throw RuntimeException("Equipment not found with id: $id") }

        val updated = equipment.copy(status = newStatus)
        val saved = equipmentRepository.save(updated)
        return EquipmentResponse.from(saved)
    }

    fun update(
        id: Long,
        request: CreateEquipmentRequest,
    ): EquipmentResponse {
        // Проверяем уникальность инвентарного номера (кроме текущего оборудования)
        if (equipmentRepository.existsByInventoryNumber(request.inventoryNumber)) {
            throw IllegalArgumentException("Оборудование с инвентарным номером ${request.inventoryNumber} уже существует")
        }

        val equipment =
            equipmentRepository
                .findById(id)
                .orElseThrow { throw RuntimeException("Equipment not found with id: $id") }

        val updated =
            equipment.copy(
                name = request.name,
                inventoryNumber = request.inventoryNumber,
                status = request.status,
                location = request.location,
            )
        val saved = equipmentRepository.save(updated)
        return EquipmentResponse.from(saved)
    }
}
