package com.ChickenKitchen.app.repository.step

import com.ChickenKitchen.app.model.entity.step.Step
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface StepRepository : JpaRepository<Step, Long> {
}