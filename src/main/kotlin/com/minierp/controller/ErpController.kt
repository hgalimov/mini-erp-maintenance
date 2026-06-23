package com.minierp.controller

import com.minierp.dto.CreateEquipmentRequest
import com.minierp.dto.CreateTechnicianRequest
import com.minierp.dto.CreateWorkOrderRequest
import com.minierp.dto.EquipmentResponse
import com.minierp.dto.TechnicianResponse
import com.minierp.dto.UpdateWorkOrderStatusRequest
import com.minierp.dto.WorkOrderResponse
import com.minierp.service.EquipmentService
import com.minierp.service.TechnicianService
import com.minierp.service.WorkOrderService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class ErpController(
    private val equipmentService: EquipmentService,
    private val technicianService: TechnicianService,
    private val workOrderService: WorkOrderService,
) {
    // Equipment
    @GetMapping("/equipment")
    fun getAllEquipment(): ResponseEntity<List<EquipmentResponse>> = ResponseEntity.ok(equipmentService.getAll())

    @GetMapping("/equipment/{id}")
    fun getEquipment(
        @PathVariable id: Long,
    ): ResponseEntity<EquipmentResponse> = ResponseEntity.ok(equipmentService.getById(id))

    @PostMapping("/equipment")
    fun createEquipment(
        @Valid @RequestBody request: CreateEquipmentRequest,
    ): ResponseEntity<EquipmentResponse> = ResponseEntity.status(HttpStatus.CREATED).body(equipmentService.create(request))

    @PatchMapping("/equipment/{id}/status")
    fun updateEquipmentStatus(
        @PathVariable id: Long,
        @RequestParam status: String,
    ): ResponseEntity<EquipmentResponse> = ResponseEntity.ok(equipmentService.updateStatus(id, status))

    // Technicians
    @GetMapping("/technicians")
    fun getAllTechnicians(): ResponseEntity<List<TechnicianResponse>> = ResponseEntity.ok(technicianService.getAll())

    @PostMapping("/technicians")
    fun createTechnician(
        @Valid @RequestBody request: CreateTechnicianRequest,
    ): ResponseEntity<TechnicianResponse> = ResponseEntity.status(HttpStatus.CREATED).body(technicianService.create(request))

    @PatchMapping("/technicians/{id}/toggle-active")
    fun toggleTechnicianStatus(
        @PathVariable id: Long,
    ): ResponseEntity<TechnicianResponse> = ResponseEntity.ok(technicianService.toggleActiveStatus(id))

    // Work Orders
    @GetMapping("/work-orders")
    fun getAllWorkOrders(
        @RequestParam(required = false) status: String?,
    ): ResponseEntity<List<WorkOrderResponse>> {
        val response = if (status != null) workOrderService.getByStatus(status) else workOrderService.getAll()
        return ResponseEntity.ok(response)
    }

    @PostMapping("/work-orders")
    fun createWorkOrder(
        @Valid @RequestBody request: CreateWorkOrderRequest,
    ): ResponseEntity<WorkOrderResponse> = ResponseEntity.status(HttpStatus.CREATED).body(workOrderService.create(request))

    @PatchMapping("/work-orders/{id}/status")
    fun updateWorkOrderStatus(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateWorkOrderStatusRequest,
    ): ResponseEntity<WorkOrderResponse> = ResponseEntity.ok(workOrderService.updateStatus(id, request.status))
}
