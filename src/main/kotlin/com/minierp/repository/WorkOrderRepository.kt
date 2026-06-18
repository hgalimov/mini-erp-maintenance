package com.minierp.repository
import com.minierp.domain.WorkOrder
import com.minierp.domain.WorkOrderStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WorkOrderRepository : JpaRepository<WorkOrder, Long> {
    fun findAllByStatus(status: WorkOrderStatus): List<WorkOrder>
}
