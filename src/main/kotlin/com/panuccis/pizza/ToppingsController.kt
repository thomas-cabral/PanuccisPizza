package com.panuccis.pizza

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class ToppingsController {
    private val toppingsService = ToppingsFileWriter()
    private val toppings = toppingsService.readToppings().mapValues { it.value }
    @PostMapping("/toppings")
    fun addTopping(@RequestBody request: PostToppingRequest): ResponseEntity<String> {
        if (!isEmailValid(request.email)) {
            return ResponseEntity.badRequest().body("Invalid email")
        }
        toppingsService.writeToppings(request.email, request.toppings)
        return ResponseEntity.ok("Topping added successfully!")
    }

    @GetMapping("/toppings")
    fun toppings() = toppings.values.flatten().groupingBy { it }.eachCount()

    @GetMapping("/toppings/best")
    fun bestPizzaByIngredientNumber(@RequestParam total: Int): List<String> {
        val ingredients = toppings.values.flatten().groupingBy { it }.eachCount().entries
        val bestPizza = ingredients.sortedByDescending { it.value }.map { it.key }

        return bestPizza.take(total)
    }

    fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("^([a-zA-Z0-9_\\-.]+)@([a-zA-Z0-9_\\-.]+)\\.([a-zA-Z]{2,5})$")
        return emailRegex.matches(email)
    }
}

data class PostToppingRequest(val email: String, val toppings: List<String>)