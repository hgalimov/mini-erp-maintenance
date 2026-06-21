package com.minierp.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "equipment")
data class Equipment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false) val name: String,
    @Column(name = "inventory_number", unique = true, nullable = false) val inventoryNumber: String,
    @Enumerated(EnumType.STRING) @Column(nullable = false) val status: EquipmentStatus = EquipmentStatus.ACTIVE,
    @Column(nullable = false) val location: String,
)
