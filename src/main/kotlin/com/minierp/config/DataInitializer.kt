package com.minierp.config

import com.minierp.domain.Equipment
import com.minierp.domain.EquipmentStatus
import com.minierp.domain.Technician
import com.minierp.domain.WorkOrder
import com.minierp.domain.WorkOrderStatus
import com.minierp.repository.EquipmentRepository
import com.minierp.repository.TechnicianRepository
import com.minierp.repository.WorkOrderRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DataInitializer(
    private val equipmentRepository: EquipmentRepository,
    private val technicianRepository: TechnicianRepository,
    private val workOrderRepository: WorkOrderRepository,
) {
    fun initData(): CommandLineRunner =
        CommandLineRunner {
            // Создаем тестовые данные только если БД пуста
//        if (equipmentRepository.count() == 0L) {
//            val equipment1 = equipmentRepository.save(
//                Equipment(
//                    name = "Станок ЧПУ",
//                    inventoryNumber = "CNC-001",
//                    status = EquipmentStatus.ACTIVE,
//                    location = "Цех 1"
//                )
//            )
//
//            val equipment2 = equipmentRepository.save(
//                Equipment(
//                    name = "Пресс",
//                    inventoryNumber = "PRESS-001",
//                    status = EquipmentStatus.BROKEN,
//                    location = "Цех 2"
//                )
//            )
//
//            val tech1 = technicianRepository.save(
//                Technician(
//                    fullName = "Иван Петров",
//                    specialization = "Механик",
//                    isActive = true
//                )
//            )
//
//            val tech2 = technicianRepository.save(
//                Technician(
//                    fullName = "Мария Сидорова",
//                    specialization = "Электрик",
//                    isActive = true
//                )
//            )
//
//            workOrderRepository.save(
//                WorkOrder(
//                    equipment = equipment1,
//                    technician = tech1,
//                    description = "Замена масла",
//                    status = WorkOrderStatus.CREATED
//                )
//            )
//
//            println("=== Тестовые данные созданы ===")
//            println("Оборудование: ${equipmentRepository.count()}")
//            println("Мастера: ${technicianRepository.count()}")
//            println("Наряд-заказы: ${workOrderRepository.count()}")
//        }
        }
}
