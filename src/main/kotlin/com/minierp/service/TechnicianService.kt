package com.minierp.service
import com.minierp.domain.Technician
import com.minierp.dto.CreateTechnicianRequest
import com.minierp.dto.TechnicianResponse
import com.minierp.repository.TechnicianRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TechnicianService(
    private val repository: TechnicianRepository,
) {
    @Transactional(readOnly = true)
    fun getAll() = repository.findAll().map { TechnicianResponse.from(it) }

    @Transactional fun create(req: CreateTechnicianRequest) =
        TechnicianResponse.from(
            repository.save(
                Technician(
                    fullName = req.fullName,
                    specialization = req.specialization,
                    isActive = req.isActive,
                ),
            ),
        )

    @Transactional fun toggleActive(id: Long) =
        TechnicianResponse.from(
            repository
                .findById(id)
                .orElseThrow {
                    IllegalArgumentException("Technician not found")
                }.let { repository.save(it.copy(isActive = !it.isActive)) },
        )
}
