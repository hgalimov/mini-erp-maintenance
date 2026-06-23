package com.minierp.domain

object WorkOrderStatus {
    const val CREATED = "CREATED"
    const val IN_PROGRESS = "IN_PROGRESS"
    const val COMPLETED = "COMPLETED"
    const val CANCELLED = "CANCELLED"

    fun isValid(status: String): Boolean = setOf(CREATED, IN_PROGRESS, COMPLETED, CANCELLED).contains(status)
}
