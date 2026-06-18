package com.minierp.controller

import com.minierp.domain.EquipmentStatus
import com.minierp.domain.WorkOrderStatus
import com.minierp.dto.*
import com.minierp.service.EquipmentService
import com.minierp.service.TechnicianService
import com.minierp.service.WorkOrderService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/")
class WebController(
    private val equipService: EquipmentService,
    private val techService: TechnicianService,
    private val workOrderService: WorkOrderService
) {

    @GetMapping
    fun index(model: Model): String {
        model.addAttribute("equipmentCount", equipService.getAll().size)
        model.addAttribute("technicianCount", techService.getAll().size)
        model.addAttribute("workOrderCount", workOrderService.getAll().size)
        model.addAttribute("activeOrders", workOrderService.getByStatus(WorkOrderStatus.IN_PROGRESS).size)
        return "index"
    }

    // --- Equipment ---
    @GetMapping("/equipment")
    fun equipmentPage(model: Model): String {
        model.addAttribute("equipment", equipService.getAll())
        model.addAttribute("statuses", EquipmentStatus.values())
        model.addAttribute("newEquipment", CreateEquipmentRequest("", "", EquipmentStatus.ACTIVE, ""))
        return "equipment"
    }

    @PostMapping("/equipment")
    fun createEquipment(@ModelAttribute newEquipment: CreateEquipmentRequest, redirect: RedirectAttributes): String {
        return try {
            equipService.create(newEquipment)
            redirect.addFlashAttribute("success", "Equipment created successfully")
            "redirect:/equipment"
        } catch (e: Exception) {
            redirect.addFlashAttribute("error", e.message)
            "redirect:/equipment"
        }
    }

    @PostMapping("/equipment/{id}/status")
    fun updateEquipmentStatus(@PathVariable id: Long, @RequestParam status: EquipmentStatus, redirect: RedirectAttributes): String {
        return try {
            equipService.updateStatus(id, status)
            redirect.addFlashAttribute("success", "Status updated")
            "redirect:/equipment"
        } catch (e: Exception) {
            redirect.addFlashAttribute("error", e.message)
            "redirect:/equipment"
        }
    }

    // --- Technicians ---
    @GetMapping("/technicians")
    fun techniciansPage(model: Model): String {
        model.addAttribute("technicians", techService.getAll())
        model.addAttribute("newTechnician", CreateTechnicianRequest("", "", true))
        return "technicians"
    }

    @PostMapping("/technicians")
    fun createTechnician(@ModelAttribute newTechnician: CreateTechnicianRequest, redirect: RedirectAttributes): String {
        return try {
            techService.create(newTechnician)
            redirect.addFlashAttribute("success", "Technician added")
            "redirect:/technicians"
        } catch (e: Exception) {
            redirect.addFlashAttribute("error", e.message)
            "redirect:/technicians"
        }
    }

    @PostMapping("/technicians/{id}/toggle")
    fun toggleTechnician(@PathVariable id: Long, redirect: RedirectAttributes): String {
        return try {
            techService.toggleActive(id)
            redirect.addFlashAttribute("success", "Status toggled")
            "redirect:/technicians"
        } catch (e: Exception) {
            redirect.addFlashAttribute("error", e.message)
            "redirect:/technicians"
        }
    }

    // --- Work Orders ---
    @GetMapping("/work-orders")
    fun workOrdersPage(model: Model): String {
        model.addAttribute("workOrders", workOrderService.getAll())
        model.addAttribute("equipment", equipService.getAll())
        model.addAttribute("technicians", techService.getAll())
        model.addAttribute("statuses", WorkOrderStatus.values())
        model.addAttribute("newOrder", CreateWorkOrderRequest(0, 0, ""))
        return "work-orders"
    }

    @PostMapping("/work-orders")
    fun createWorkOrder(@ModelAttribute newOrder: CreateWorkOrderRequest, redirect: RedirectAttributes): String {
        return try {
            workOrderService.create(newOrder)
            redirect.addFlashAttribute("success", "Work order created")
            "redirect:/work-orders"
        } catch (e: Exception) {
            redirect.addFlashAttribute("error", e.message)
            "redirect:/work-orders"
        }
    }

    @PostMapping("/work-orders/{id}/status")
    fun updateWorkOrderStatus(@PathVariable id: Long, @RequestParam status: WorkOrderStatus, redirect: RedirectAttributes): String {
        return try {
            workOrderService.updateStatus(id, status)
            redirect.addFlashAttribute("success", "Status updated")
            "redirect:/work-orders"
        } catch (e: Exception) {
            redirect.addFlashAttribute("error", e.message)
            "redirect:/work-orders"
        }
    }
}