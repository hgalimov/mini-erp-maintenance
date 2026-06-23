package com.minierp.domain

object EquipmentStatus {
    const val ACTIVE = "ACTIVE"
    const val BROKEN = "BROKEN"
    const val MAINTENANCE = "MAINTENANCE"
    const val DECOMMISSIONED = "DECOMMISSIONED"

    fun isValid(status: String): Boolean = setOf(ACTIVE, BROKEN, MAINTENANCE, DECOMMISSIONED).contains(status)
}
