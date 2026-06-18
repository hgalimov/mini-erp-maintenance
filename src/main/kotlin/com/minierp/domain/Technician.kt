package com.minierp.domain

import jakarta.persistence.*

@Entity
@Table(name = "technicians")
data class Technician(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false) val fullName: String,
    @Column(nullable = false) val specialization: String,
    @Column(nullable = false) val isActive: Boolean = true
)