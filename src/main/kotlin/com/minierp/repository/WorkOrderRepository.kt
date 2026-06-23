package com.minierp.repository

import com.minierp.domain.WorkOrder
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WorkOrderRepository : JpaRepository<WorkOrder, Long> {
    @EntityGraph(attributePaths = ["equipment", "technician"])
    override fun findAll(): List<WorkOrder>

    @EntityGraph(attributePaths = ["equipment", "technician"])
    fun findAllByStatus(status: String): List<WorkOrder>
}
