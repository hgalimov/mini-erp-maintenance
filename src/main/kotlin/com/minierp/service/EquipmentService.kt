package com.minierp.service
import com.minierp.domain.Equipment
import com.minierp.domain.EquipmentStatus
import com.minierp.dto.CreateEquipmentRequest
import com.minierp.dto.EquipmentResponse
import com.minierp.repository.EquipmentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EquipmentService(
    private val equipmentRepository: EquipmentRepository,
) {
    @Transactional(readOnly = true)
    fun getAll() = equipmentRepository.findAll().map { EquipmentResponse.from(it) }

    @Transactional(readOnly = true)
    fun getById(id: Long) =
        EquipmentResponse.from(
            equipmentRepository.findById(id).orElseThrow {
                IllegalArgumentException("Equipment not found")
            },
        )

    @Transactional
    fun create(req: CreateEquipmentRequest): EquipmentResponse {
        if (equipmentRepository.existsByInventoryNumber(req.inventoryNumber)) throw IllegalArgumentException("Inventory number exists")
        val saved =
            equipmentRepository.save(
                Equipment(
                    name = req.name,
                    inventoryNumber = req.inventoryNumber,
                    status = req.status,
                    location = req.location,
                ),
            )
        return EquipmentResponse.from(saved)
    }

    @Transactional
    fun updateStatus(
        id: Long,
        status: EquipmentStatus,
    ) = EquipmentResponse.from(
        equipmentRepository
            .findById(id)
            .orElseThrow {
                IllegalArgumentException("Equipment not found")
            }.let { equipmentRepository.save(it.copy(status = status)) },
    )
}
