package com.minierp.controller
import com.minierp.domain.EquipmentStatus
import com.minierp.domain.WorkOrderStatus
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
    private val equipService: EquipmentService,
    private val techService: TechnicianService,
    private val workOrderService: WorkOrderService,
) {
    @GetMapping("/equipment")
    fun getAllEquipment() = ResponseEntity.ok(equipService.getAll())

    @GetMapping("/equipment/{id}")
    fun getEquipment(
        @PathVariable id: Long,
    ) = ResponseEntity.ok(equipService.getById(id))

    @PostMapping("/equipment")
    fun createEquipment(
        @Valid @RequestBody req: CreateEquipmentRequest,
    ) = ResponseEntity.status(HttpStatus.CREATED).body(equipService.create(req))

    @PatchMapping("/equipment/{id}/status")
    fun updateEquipmentStatus(
        @PathVariable id: Long,
        @RequestParam status: EquipmentStatus,
    ) = ResponseEntity.ok(equipService.updateStatus(id, status))

    @GetMapping("/technicians")
    fun getAllTechnicians() = ResponseEntity.ok(techService.getAll())

    @PostMapping("/technicians")
    fun createTechnician(
        @Valid @RequestBody req: CreateTechnicianRequest,
    ) = ResponseEntity.status(HttpStatus.CREATED).body(techService.create(req))

    @PatchMapping("/technicians/{id}/toggle-active")
    fun toggleTechnician(
        @PathVariable id: Long,
    ) = ResponseEntity.ok(techService.toggleActive(id))

    @GetMapping("/work-orders")
    fun getAllWorkOrders(
        @RequestParam(required = false) status: WorkOrderStatus?,
    ) = ResponseEntity.ok(if (status != null) workOrderService.getByStatus(status) else workOrderService.getAll())

    @PostMapping("/work-orders")
    fun createWorkOrder(
        @Valid @RequestBody req: CreateWorkOrderRequest,
    ) = ResponseEntity.status(HttpStatus.CREATED).body(workOrderService.create(req))

    @PatchMapping("/work-orders/{id}/status")
    fun updateWorkOrderStatus(
        @PathVariable id: Long,
        @Valid @RequestBody req: UpdateWorkOrderStatusRequest,
    ) = ResponseEntity.ok(workOrderService.updateStatus(id, req.status))
}
