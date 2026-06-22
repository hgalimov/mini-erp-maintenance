package com.minierp.controller

import com.minierp.domain.EquipmentStatus
import com.minierp.domain.WorkOrderStatus
import com.minierp.dto.CreateEquipmentRequest
import com.minierp.dto.CreateTechnicianRequest
import com.minierp.dto.CreateWorkOrderRequest
import com.minierp.dto.EquipmentResponse
import com.minierp.dto.TechnicianResponse
import com.minierp.service.EquipmentService
import com.minierp.service.TechnicianService
import com.minierp.service.WorkOrderService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/web")
class WebController(
    private val equipmentService: EquipmentService,
    private val technicianService: TechnicianService,
    private val workOrderService: WorkOrderService,
) {
    // ========== ГЛАВНАЯ СТРАНИЦА ==========
    @GetMapping("/")
    fun home(): String = "redirect:/web/home"

    @GetMapping("/home")
    fun homePage(): String = "index"

    // ========== ОБОРУДОВАНИЕ ==========
    @GetMapping("/equipment")
    fun equipmentList(model: Model): String {
        val equipment: List<EquipmentResponse> = equipmentService.getAll()
        model.addAttribute("equipment", equipment)
        return "equipment/list"
    }

    @GetMapping("/equipment/new")
    fun newEquipmentForm(model: Model): String {
        model.addAttribute("editMode", false)
        return "equipment/form"
    }

    @PostMapping("/equipment")
    fun createEquipment(
        @RequestParam name: String,
        @RequestParam inventoryNumber: String,
        @RequestParam status: EquipmentStatus,
        @RequestParam location: String,
        redirectAttributes: RedirectAttributes,
    ): String {
        try {
            val request = CreateEquipmentRequest(name, inventoryNumber, status, location)
            equipmentService.create(request)
            redirectAttributes.addFlashAttribute("success", "Оборудование успешно создано")
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: ${e.message}")
        }
        return "redirect:/web/equipment"
    }

    @GetMapping("/equipment/{id}/edit")
    fun editEquipmentForm(
        @PathVariable id: Long,
        model: Model,
    ): String {
        val equipment: EquipmentResponse = equipmentService.getById(id)
        model.addAttribute("equipment", equipment)
        model.addAttribute("editMode", true)
        return "equipment/form"
    }

    @PostMapping("/equipment/{id}")
    fun updateEquipment(
        @PathVariable id: Long,
        @RequestParam status: EquipmentStatus,
        redirectAttributes: RedirectAttributes,
    ): String {
        try {
//            equipmentService.updateStatus(id, status)
            redirectAttributes.addFlashAttribute("success", "Статус обновлён")
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: ${e.message}")
        }
        return "redirect:/web/equipment"
    }

    // ========== МАСТЕРА ==========
    @GetMapping("/technicians")
    fun techniciansList(model: Model): String {
        val technicians: List<TechnicianResponse> = technicianService.getAll()
        model.addAttribute("technicians", technicians)
        return "technician/list"
    }

    @GetMapping("/technicians/new")
    fun newTechnicianForm(): String = "technician/form"

    @PostMapping("/technicians")
    fun createTechnician(
        @RequestParam fullName: String,
        @RequestParam specialization: String,
        @RequestParam(required = false) isActive: Boolean?, // ← nullable Boolean
        redirectAttributes: RedirectAttributes,
    ): String {
        try {
            // Если чекбокс не отмечен, isActive будет null → считаем false
            val request =
                CreateTechnicianRequest(
                    fullName = fullName,
                    specialization = specialization,
                    isActive = isActive ?: false, // ← null превращаем в false
                )
            technicianService.create(request)
            redirectAttributes.addFlashAttribute("success", "Мастер успешно создан")
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: ${e.message}")
        }
        return "redirect:/web/technicians"
    }

    @PostMapping("/technicians/{id}/toggle")
    fun toggleTechnician(
        @PathVariable id: Long,
        redirectAttributes: RedirectAttributes,
    ): String {
        try {
            technicianService.toggleActive(id)
            redirectAttributes.addFlashAttribute("success", "Статус активности изменён")
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: ${e.message}")
        }
        return "redirect:/web/technicians"
    }

    // ========== НАРЯД-ЗАКАЗЫ ==========
    @GetMapping("/work-orders")
    fun workOrdersList(model: Model): String {
        val workOrders = workOrderService.getAll()
        model.addAttribute("workOrders", workOrders)
        return "workorder/list"
    }

    @GetMapping("/work-orders/new")
    fun newWorkOrderForm(model: Model): String {
        val equipment: List<EquipmentResponse> = equipmentService.getAll()
        val technicians: List<TechnicianResponse> = technicianService.getAll().filter { it.isActive }
        model.addAttribute("equipment", equipment)
        model.addAttribute("technicians", technicians)
        return "workorder/form"
    }

    @PostMapping("/work-orders")
    fun createWorkOrder(
        @RequestParam equipmentId: Long,
        @RequestParam technicianId: Long,
        @RequestParam description: String,
        redirectAttributes: RedirectAttributes,
    ): String {
        try {
            val request = CreateWorkOrderRequest(equipmentId, technicianId, description)
            workOrderService.create(request)
            redirectAttributes.addFlashAttribute("success", "Наряд-заказ создан")
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: ${e.message}")
        }
        return "redirect:/web/work-orders"
    }

    @PostMapping("/work-orders/{id}/status")
    fun updateWorkOrderStatus(
        @PathVariable id: Long,
        @RequestParam status: WorkOrderStatus,
        redirectAttributes: RedirectAttributes,
    ): String {
        try {
            workOrderService.updateStatus(id, status)
            redirectAttributes.addFlashAttribute("success", "Статус обновлён")
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: ${e.message}")
        }
        return "redirect:/web/work-orders"
    }
}
