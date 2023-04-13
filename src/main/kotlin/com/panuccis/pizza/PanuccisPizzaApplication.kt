package com.panuccis.pizza

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PanuccisPizzaApplication

fun main(args: Array<String>) {
    runApplication<PanuccisPizzaApplication>(*args)
}
