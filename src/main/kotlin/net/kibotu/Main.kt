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

// region https://www.codingame.com/ide/puzzle/don't-panic

fun mainDontPanic(args: Array<String>) {

    val nbFloors = 1 // number of floors
    val width = 13 // width of the area
    val nbRounds = 100 // maximum number of rounds
    val exitFloor = 1 // floor on which the exit is found
    val exitPos = 3 // position of the exit on its floor
    val nbTotalClones = 10 // number of generated clones
    val nbAdditionalElevators = 0 // ignore (always zero)
    val nbElevators = 1 // number of elevators

    val elevatorPositions = listOf(9)
    val elevatorFloors = listOf(0)
    val clones = listOf(Clone(0, 5, "RIGHT"))

    println("nbFloors=$nbFloors width=$width nbRounds=$nbRounds exitFloor=$exitFloor exitPos=$exitPos nbTotalClones=$nbTotalClones nbAdditionalElevators=$nbAdditionalElevators nbElevators=$nbElevators")

    val exits = (0 until nbElevators).map { elevatorFloors[it] to elevatorPositions[it] }.toMutableList()
    exits.add(exitFloor to exitPos)
    println(exits)

    val turns = 22

    for (i in 0 until turns) {
        val cloneFloor = clones[0].cloneFloor // floor of the leading clone
        val clonePos = clones[0].clonePos // position of the leading clone on its floor
        val direction = clones[0].direction // direction of the leading clone: LEFT or RIGHT

        val exit = exits.find { it.first == cloneFloor }?.second ?: 0
        println("cloneFloor=$cloneFloor clonePos=$clonePos direction=$direction exit=$exit")

        // based on floor and direction either block or wait
        when {
            direction == "NONE" -> println("WAIT")
            clonePos > exit -> println(if (direction == "LEFT") "WAIT" else "BLOCK")
            clonePos < exit -> println(if (direction == "RIGHT") "WAIT" else "BLOCK")
            else -> println("WAIT")
        }
    }

}

data class Clone(var cloneFloor: Int, var clonePos: Int, var direction: String)

// endregion

// region https://www.codingame.com/ide/puzzle/codingame-sponsored-contest https://github.com/niconoe-/Codingame/blob/master/1%20-%20Puzzles/02%20-%20Optimization%20Puzzles/CodinGame%20Sponsored%20Challenge/PHP.php
fun main2(args: Array<String>) {

    val input = Scanner(System.`in`)
    val width = input.nextInt()
    val height = input.nextInt()
    val amountPlayer = input.nextInt()

    val input5 = if (input.hasNextLine()) {
        input.nextLine()
    } else ""

    val hasPath = "_"
    val wall = "#"
    val unkown = "?"

    val player = listOf("A", "B", "C", "D", "X")

    /*
        C
      E B A
        D
     */

    val map: Array<Array<String?>> = Array(width) { arrayOfNulls<String>(height) }

    for (i in 0 until map.size) {
        for (j in 0 until map[i].size) {
            map[i][j] = "_ "
        }
    }

    while (true) {
        val up = input.nextLine()
        val right = input.nextLine()
        val down = input.nextLine()
        val left = input.nextLine()

        val playerPositions = (0 until amountPlayer).map { input.nextInt() to input.nextInt() }.toList()

        val x = playerPositions.last().first
        val y = playerPositions.last().second

        // up
        if (x > 0 && x < map.size && y > 0 && y < map[0].size)
            map[x][y - 1] = "$up "

        // right
        if (x > 0 && x < map.size && y > 0 && y < map[0].size)
            map[x + 1][y] = "$right "

        // down
        if (x > 0 && x < map.size && y > 0 && y < map[0].size)
            map[x][y + 1] = "$down "

        // left
        if (x > 0 && x < map.size && y > 0 && y < map[0].size)
            map[x - 1][y] = "$left "

        val playerMap = map.copy()

        for (i in 0 until amountPlayer) {
            val px = playerPositions[i].first
            val py = playerPositions[i].second
            if (px > 0 && px < map.size && py > 0 && py < map[0].size)
                playerMap[px][py] = "${player[i]}($px,$py) "
        }

        for (i in 0 until height) {
            for (j in 0 until width) {
                System.err.print(playerMap[i][j])
            }
            System.err.println()
        }

        val input7 = input.nextLine()

        val actions = mutableListOf<Action>()

        if (up == hasPath)
            actions.add(Action.UP)
        if (right == hasPath)
            actions.add(Action.RIGHT)
        if (down == hasPath)
            actions.add(Action.DOWN)
        if (left == hasPath)
            actions.add(Action.LEFT)

        System.err.println("[w=$width,h=$height] ${if (!input5.isNullOrEmpty()) "input5=$input5 " else ""}${if (!input7.isNullOrEmpty()) "input7=$input7 " else ""}top: $up right: $right down: $down left= $left")
        System.err.println("available paths: ${if (up == hasPath) "top " else ""}${if (right == hasPath) "right " else ""}${if (down == hasPath) "down " else ""}${if (left == hasPath) "left" else ""}")
        playerPositions.forEachIndexed { index, pair -> System.err.println("${player[index]} $pair") }
        val action = actions.random(Random())
        System.err.println("move=$action(${action?.value})")
        action?.move()
    }
}

enum class Action(val value: String) {
    RIGHT("A"),
    STAY("B"),
    UP("C"),
    DOWN("D"),
    LEFT("E");

    fun move() = println(value)
}

fun Array<Array<String?>>.copy() = Array(size) { get(it).clone() }

/**
 * Returns a random element using the specified [random] instance as the source of randomness.
 */
fun <E> List<E>.random(random: java.util.Random): E? = if (size > 0) get(random.nextInt(size)) else null

// endregion

//region 003

/*
01 Test 1
Holiday                             HyoaldiidlaoyH
02 Test 2
Hello World!                        H!edlllroo WW oorlllde!H
03 Test 3
Space, then final frontier ...      S.p.a.c er,e itthneonr ff ilnaanli ff rnoenhtti e,re c.a.p.S
04 Test 4

Sussex result matter any end see. It speedily me addition weddings vicinity in pleasure. Happiness commanded an conveying breakfast in. Regard her say warmly elinor. Him these are visit front end for seven walls. Money eat scale now ask law learn. Side its they just any upon see last. He prepared no shutters perceive do greatest. Ye at unpleasant solicitude in companions interested.

S.udsestesxe rreetsnuil ts nmoaitntaeprm oacn yn ie nedd usteiec.i lIots  stpneaesdaiellyp nmue  taad deiYt i.otns ewteadedrign gosd  veivciiencirteyp  isnr eptlteuahssu roen.  dHearpappienreps se Hc o.mtmsaanld eede sa nn ocpoun vyenyai ntgs ubjr eyaekhfta sstt ii ne.d iRSe g.anrrda ehle rw asla yk swaa rwmolny  eellaicnso rt.a eH iyme ntohMe s.es lalraew  vniesviets  frroofn td neen dt nfoorrf  steivseinv  wearlal se.s eMhotn emyi He a.tr osnciallee  ynlomwr aaws ky alsa wr elhe adrrna.g eSRi d.en ii ttss atfhkeaye rjbu sgtn iayneyv nuopco nn as edee dlnaasmtm.o cH es sperneippapraeHd  .neor usshauetltpe rnsi  pyetricneiicviev  dsog ngirdedaetwe snto.i tYied daat  eumn pylleiadseaenpts  stoIl i.ceietsu dden ei ny ncao mrpeatntiaomn st liunsteerr exsetsesdu.S

 */
fun main004(args: Array<String>) {

    // val input = Scanner(System.`in`)
    val s = "Holiday"
    val r = s.reversed()
    println(s.asSequence()
            .mapIndexed { index, c -> "$c${r[index]}" }
            .joinToString(separator = ""))
}

// endregion

// region 005

/**
 * Count the number of ones in the binary representation of each given integer.

Input
Line 1 : The number of values N to handle.
N next lines : An integer X on each line.

Output
For each integer X, the number of ones in its binary representation.

Constraints
1≤N<10
0≤X<2^32
Example

Input
3
5
2
7
Output
2
1
3
 */
fun main005(args: Array<String>) {
    val input = Scanner(System.`in`)
    val n = listOf(3L, 5L, 2L, 7L)
    for (i in 0 until n.size) {
        val x = n[i]
        println("${x.toString(2).count { it == '1' }}")
    }
}

// endregion

// region 006


// endregion