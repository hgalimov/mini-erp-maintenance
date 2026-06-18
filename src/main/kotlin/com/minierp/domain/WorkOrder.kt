package com.minierp.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "work_orders")
data class WorkOrder(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "equipment_id", nullable = false) val equipment: Equipment,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "technician_id", nullable = false) val technician: Technician,
    @Column(nullable = false, length = 1000) val description: String,
    @Enumerated(EnumType.STRING) @Column(nullable = false) val status: WorkOrderStatus = WorkOrderStatus.CREATED,
    @Column(name = "created_at", nullable = false, updatable = false) val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(name = "completed_at") var completedAt: LocalDateTime? = null
)