package com.panuccis.pizza

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class ToppingsController {
    val emails = mutableMapOf<String, Set<String>>()
    @PostMapping("/")
    fun addTopping(@RequestBody request: PostToppingRequest): ResponseEntity<String> {
        println("Email: ${request.email}")
        println("Toppings: ${request.toppings}")
        emails[request.email] = request.toppings.map { it.lowercase(Locale.getDefault()) }.toSet()
        return ResponseEntity.ok("Topping added successfully!")
    }

    @GetMapping("/")
    fun toppings() = emails.values.flatten().groupingBy { it }.eachCount()
}

data class PostToppingRequest(val email: String, val toppings: List<String>)