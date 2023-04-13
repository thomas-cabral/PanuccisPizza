package com.panuccis.pizza

import java.io.File

class ToppingsFileWriter(private val fileName: String = "toppings.txt") {

    init {
        val file = File(fileName)
        if (!file.exists()) {
            file.createNewFile()
        }
    }
    fun writeToppings(email: String, toppings: List<String>) {
        val file = File(fileName)
        val lines = file.readLines().toMutableList()
        val index = lines.indexOfFirst { it.startsWith(email) }
        if (index != -1) {
            lines.removeAt(index)
        }
        lines.add("$email:${toppings.joinToString(",")}")

        file.writeText(lines.joinToString("\n"))
    }

    fun readToppings(): Map<String, Set<String>> {
        val file = File(fileName).absoluteFile
        return file.readLines().map {
            val (email, toppings) = it.split(":")
            email to toppings.split(",").toSet()
        }.toMap()
    }
}