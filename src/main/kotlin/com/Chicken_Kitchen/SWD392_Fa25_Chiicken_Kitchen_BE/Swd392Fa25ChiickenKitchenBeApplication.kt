package com.Chicken_Kitchen.SWD392_Fa25_Chiicken_Kitchen_BE

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication

@SpringBootApplication
@EntityScan("com.Chicken_Kitchen.model.entity")
class Swd392Fa25ChiickenKitchenBeApplication

fun main(args: Array<String>) {
	runApplication<Swd392Fa25ChiickenKitchenBeApplication>(*args)
}
