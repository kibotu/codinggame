package net.kibotu

import java.util.*

// simulator https://github.com/WillNess210/LoCMSimulator
// https://github.com/WillNess210/LoCMSimulator/blob/master/sources/main.cpp

// player https://github.com/WillNess210/Legends-of-Code-and-Magic/blob/master/LCM/src/Player.java

fun main(args : Array<String>) {
    val input = Scanner(System.`in`)

    // game loop
    while (true) {
        for (i in 0 until 2) {
            val playerHealth = input.nextInt()
            val playerMana = input.nextInt()
            val playerDeck = input.nextInt()
            val playerRune = input.nextInt()
            val playerDraw = input.nextInt()
        }
        val opponentHand = input.nextInt()
        val opponentActions = input.nextInt()
        if (input.hasNextLine()) {
            input.nextLine()
        }
        for (i in 0 until opponentActions) {
            val cardNumberAndAction = input.nextLine()
        }
        val cardCount = input.nextInt()
        for (i in 0 until cardCount) {
            val cardNumber = input.nextInt()
            val instanceId = input.nextInt()
            val location = input.nextInt()
            val cardType = input.nextInt()
            val cost = input.nextInt()
            val attack = input.nextInt()
            val defense = input.nextInt()
            val abilities = input.next()
            val myHealthChange = input.nextInt()
            val opponentHealthChange = input.nextInt()
            val cardDraw = input.nextInt()
        }

        // Write an action using println()
        // To debug: System.err.println("Debug messages...");

        println("PASS")
    }
}