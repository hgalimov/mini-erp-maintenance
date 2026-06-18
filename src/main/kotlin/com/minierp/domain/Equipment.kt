package com.minierp.domain

import jakarta.persistence.*

@Entity
@Table(name = "equipment")
data class Equipment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false) val name: String,
    @Column(name = "inventory_number", unique = true, nullable = false) val inventoryNumber: String,
    @Enumerated(EnumType.STRING) @Column(nullable = false) val status: EquipmentStatus = EquipmentStatus.ACTIVE,
    @Column(nullable = false) val location: String
)