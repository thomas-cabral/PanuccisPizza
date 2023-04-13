package com.panuccis.pizza

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.io.File

@SpringBootTest
class PanuccisPizzaApplicationTests {

    @BeforeEach
    fun setup() {
        File("test.txt").createNewFile()
    }

    @AfterEach
    fun teardown() {
        File("test.txt").delete()
    }

    @Test
    fun contextLoads() {
    }

    @Test
    fun `should only save most recent by email`() {
        val toppingsService = ToppingsFileWriter("test.txt")
        toppingsService.writeToppings("test@test.com", listOf("pepperoni", "mushrooms"))
        toppingsService.writeToppings("test@test.com", listOf("pepperoni", "mushrooms", "onions"))
        toppingsService.writeToppings("test2@test.com", listOf("pepperoni", "mushrooms", "onions"))
        toppingsService.writeToppings("test@test.com", listOf("pepperoni", "mushrooms"))

        val toppingsRecords = toppingsService.readToppings()
        // assert count of toppings
        assert(toppingsRecords.size == 2)
    }

    @Test
    fun `should read toppings`() {
        val toppingsService = ToppingsFileWriter("test.txt")
        toppingsService.writeToppings("test2@test.com", listOf("pepperoni", "mushrooms", "onions"))
        toppingsService.writeToppings("test@test.com", listOf("pepperoni", "mushrooms"))

        val toppingsRecords = toppingsService.readToppings()
        val totalToppings = toppingsRecords.values.flatten()
        // assert count of toppings
        assert(toppingsRecords.size == 2)
        // should have 3 unique toppings, 5 total
        assert(totalToppings.size == 5)
        assert(totalToppings.distinct().size == 3)
    }

    @Test
    fun `should have correct count for each topping`() {
        val toppingsService = ToppingsFileWriter("test.txt")
        toppingsService.writeToppings("test2@test.com", listOf("pepperoni", "mushrooms", "onions"))
        toppingsService.writeToppings("test@test.com", listOf("pepperoni", "mushrooms"))

        val toppingsRecords = toppingsService.readToppings()
        val totalToppings = toppingsRecords.values.flatten().groupingBy { it }.eachCount()

        assert(totalToppings["pepperoni"] == 2)
        assert(totalToppings["mushrooms"] == 2)
        assert(totalToppings["onions"] == 1)
    }
}
