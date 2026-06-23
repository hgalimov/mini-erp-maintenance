package com.minierp.service

import com.minierp.domain.Technician
import com.minierp.dto.CreateTechnicianRequest
import com.minierp.dto.TechnicianResponse
import com.minierp.repository.TechnicianRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TechnicianService(
    private val technicianRepository: TechnicianRepository,
) {
    fun create(request: CreateTechnicianRequest): TechnicianResponse {
        val entity =
            Technician(
                fullName = request.fullName,
                specialization = request.specialization,
                isActive = request.isActive,
            )
        val saved = technicianRepository.save(entity)
        return TechnicianResponse.from(saved)
    }

    fun getAll(): List<TechnicianResponse> = technicianRepository.findAll().map { TechnicianResponse.from(it) }

    fun toggleActiveStatus(id: Long): TechnicianResponse {
        val technician =
            technicianRepository
                .findById(id)
                .orElseThrow { throw RuntimeException("Technician not found with id: $id") }

        val updated = technician.copy(isActive = !technician.isActive)
        val saved = technicianRepository.save(updated)
        return TechnicianResponse.from(saved)
    }
}
