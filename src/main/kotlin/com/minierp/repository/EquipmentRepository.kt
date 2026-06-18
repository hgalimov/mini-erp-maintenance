package com.minierp.repository

import com.minierp.domain.Equipment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EquipmentRepository : JpaRepository<Equipment, Long> {
    fun existsByInventoryNumber(inventoryNumber: String): Boolean
}
