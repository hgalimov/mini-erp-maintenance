package com.minierp.repository

import com.minierp.domain.Technician
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TechnicianRepository : JpaRepository<Technician, Long>
