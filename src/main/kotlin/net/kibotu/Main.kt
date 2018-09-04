package net.kibotu

import java.util.*

// region 001

fun main001(args: Array<String>) {
    val MMM = 3
    val AAA = 17
    val NNN = 4

    println(Math.max(0, Math.min((0 until NNN + 1).sumBy { it * MMM } - AAA, Int.MAX_VALUE)))
}

// endregion

fun clamp(value: Int, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): Int = Math.max(min, Math.min(value, max))

// region 002

/**
 * The existence of Lychrel number is an open mathematical problem.
 * Given a number N, repeat the process of reversing digits and adding the resulting number, until forming a palindrome.
 * If the process ends, the number is not a Lychrel number but for example, 196 is supposed to be a Lychrel number because the process seems not to end.
 */
fun main002(args: Array<String>) {

    val N = 1221L // 10

    print("$N")

    if (isPalindrom(N)) {
        return
    }

    var x = N
    while (!isPalindrom(x)) {
        x += x.toString().reversed().toLong()
        print(" $x")
    }
}

fun isPalindrom(x: Long) = x.toString() == x.toString().reversed()

// endregion

// region 003

/**
 * In a car park, cars arrive at time, stay for a while then leave (as usual). A keeper is in charge of this car park.
 * He is only payed when the car park is not empty. You want to give him an idea of his daily salary.
 * With the given dates of arrival and departure of a set of cars, you must give the number of hours with at least one car in the park.
 *
 * Input
 * Line 1: An integer N for the number of cars
 * N next lines: For each car, 2 integers Start and End for the hour of arrival (inclusive) and departure (exclusive).
 *
 * Output
 * Line 1: The number of hours with at least one car in the park.
 */
fun main003(args: Array<String>) {

    /*
    cars 3 start 2 end 5
    cars 3 start 8 end 13
    cars 3 start 14 end 20
     */
    val N = 3

    val starts = listOf(2, 8, 14)
    val ends = listOf(5, 13, 20)

    val carsInPark = mutableListOf<Int>()

    for (i in 0 until N) {
        val START = starts[i]
        val END = ends[i]

        val range = (START until END).toList()

        carsInPark.addAll(range)

        println("cars $N start $START end $END $range")
    }

    println(carsInPark.toSet().count())
}

// endregion

// region thor https://www.codingame.com/ide/puzzle/power-of-thor

fun mainThor(args: Array<String>) {

    val input = Scanner(System.`in`)
    val LX = input.nextInt() // the X position of the light of power
    val LY = input.nextInt() // the Y position of the light of power
    var TX = input.nextInt() // Thor's starting X position
    var TY = input.nextInt() // Thor's starting Y position

    while (true) {
        val remainingTurns = input.nextInt()

        if (TY > LY) {
            print("N")
            --TY
        } else if (TY < LY) {
            print("S")
            ++TY
        }

        if (TX > LX) {
            print("W")
            --TX
        } else if (TX < LX) {
            print("E")
            ++TX
        }

        println()
    }
}

// endregion

//region temperatures https://www.codingame.com/ide/puzzle/temperatures

fun mainTemperatures(args: Array<String>) {
//    val input = listOf(1, -2, -8, 4, 5)
//    val input = listOf(-12, -5, -137)
//    val input = listOf(42, -5, 12, 21, 5, 24)
    val input = listOf(42, 5, 12, 21, -5, 24)
    val n = input.size

    println((0 until n)
            .map { input[it] }
            .groupBy { Math.abs(it) }
            .toSortedMap()
            .minBy { 0 }
            ?.value
            ?.sortedDescending()
            ?.firstOrNull()
            ?: 0)
}

//endregion

fun main(args: Array<String>) {

}