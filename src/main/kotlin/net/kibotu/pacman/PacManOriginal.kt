package net.kibotu.pacman

import java.util.*

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val firstInitInput = input.nextInt()
    val secondInitInput = input.nextInt()
    val thirdInitInput = input.nextInt()
    if (input.hasNextLine()) {
        input.nextLine()
    }

    // game loop
    while (true) {
        val firstInput = input.nextLine()
        val secondInput = input.nextLine()
        val thirdInput = input.nextLine()
        val fourthInput = input.nextLine()
        for (i in 0 until thirdInitInput) {
            val fifthInput = input.nextInt()
            val sixthInput = input.nextInt()
        }
        input.nextLine()

        // Write an action using println()
        // To debug: System.err.println("Debug messages...");

        println("A, B, C, D or E")
    }
}


fun main2(args: Array<String>) {
    Game2.main()
}

object Game2 {

    fun main() {
        val input = Scanner(System.`in`)
        val height = input.nextInt()
        val width = input.nextInt()
        val amountPlayer = input.nextInt()

        val input5 = if (input.hasNextLine()) {
            input.nextLine()
        } else ""

        map = Array(width) { arrayOfNulls<Tile>(height) }

        playerMap = Array(width) { arrayOfNulls<Tile>(height) }

        for (i in 0 until map.size) {
            for (j in 0 until map[i].size) {
                map[i][j] = Tile(i, j)
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

            map[x][y]?.visited = true

// up
            if (x > 0 && x < map.size && y > 0 && y < map[0].size)
                map[x][y - 1]?.value = "$up "

// right
            if (x > 0 && x < map.size && y > 0 && y < map[0].size)
                map[x + 1][y]?.value = "$right "

// down
            if (x > 0 && x < map.size && y > 0 && y < map[0].size)
                map[x][y + 1]?.value = "$down "

// left
            if (x > 0 && x < map.size && y > 0 && y < map[0].size)
                map[x - 1][y]?.value = "$left "

            for (i in 0 until playerMap.size) {
                for (j in 0 until playerMap[i].size) {
                    playerMap[i][j] = map[i][j]?.copy()
                }
            }

            for (i in 0 until amountPlayer) {
                val px = playerPositions[i].first
                val py = playerPositions[i].second
// if (px > 0 && px < map.size && py > 0 && py < map[0].size)
                playerMap[px][py]?.value = "${player[i]} "
            }

            for (j in 0 until map[0].size) {
                for (i in 0 until map.size) {
                    System.err.print(playerMap[i][j]?.value)
                }
                System.err.println()
            }

            val input7 = input.nextLine()

            var actions = mutableSetOf<Action>()

            val currentTile = playerMap[x][y]

            /**
             *
             * Relevant: player positions trying to dodge
             *
             *  __________________
             * |   |   | 1 |   |   |
             * | - | - | - | - | - |
             * |   | 2 |   | 3 |   |
             * | - | - | - | - | - |
             * | 4 |   | X |   | 5 |
             * | - | - | - | - | - |
             * |   | 6 |   | 7 |   |
             * | - | - | - | - | - |
             * |   |   | 8 |   |   |
             *  ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾
             *
             */

            val avoid1 = currentTile?.up()?.up() // avoid: up
            val avoid2 = currentTile?.up()?.left() // avoid: up left
            val avoid3 = currentTile?.up()?.right() // avoid: up right
            val avoid4 = currentTile?.left()?.left() // avoid: left
            val avoid5 = currentTile?.right()?.right() // avoid: right
            val avoid6 = currentTile?.down()?.left() // avoid: left down
            val avoid7 = currentTile?.down()?.right() // avoid: down right
            val avoid8 = currentTile?.down()?.down() // avoid: down

            val visitedUp = playerMap[x][y]?.up()?.visited == true
            val visitedRight = playerMap[x][y]?.right()?.visited == true
            val visitedDown = playerMap[x][y]?.down()?.visited == true
            val visitedLeft = playerMap[x][y]?.left()?.visited == true

            val enemyPlayerPositions = playerPositions.take(amountPlayer - 1)

            if (up == hasPath && avoid1?.hasPlayer(enemyPlayerPositions) != true && avoid2?.hasPlayer(enemyPlayerPositions) != true && avoid3?.hasPlayer(enemyPlayerPositions) != true)
                actions.add(Action.UP)
            if (right == hasPath && avoid5?.hasPlayer(enemyPlayerPositions) != true && avoid7?.hasPlayer(enemyPlayerPositions) != true)
                actions.add(Action.RIGHT)
            if (down == hasPath && avoid8?.hasPlayer(enemyPlayerPositions) != true && avoid6?.hasPlayer(enemyPlayerPositions) != true && avoid7?.hasPlayer(enemyPlayerPositions) != true)
                actions.add(Action.DOWN)
            if (left == hasPath && avoid4?.hasPlayer(enemyPlayerPositions) != true && avoid2?.hasPlayer(enemyPlayerPositions) != true && avoid3?.hasPlayer(enemyPlayerPositions) != true && avoid6?.hasPlayer(enemyPlayerPositions) != true)
                actions.add(Action.LEFT)

            if (actions.contains(Action.UP) && !visitedUp)
                actions.add(Action.UP)
            if (actions.contains(Action.RIGHT) && !visitedRight)
                actions.add(Action.RIGHT)
            if (actions.contains(Action.DOWN) && !visitedDown)
                actions.add(Action.DOWN)
            if (actions.contains(Action.LEFT) && !visitedLeft)
                actions.add(Action.LEFT)

            if (!actions.isEmpty()) {
                if (up == hasPath && !visitedUp && avoid1?.hasPlayer(enemyPlayerPositions) != true && avoid2?.hasPlayer(enemyPlayerPositions) != true && avoid3?.hasPlayer(enemyPlayerPositions) != true)
                    actions.add(Action.UP)
                if (right == hasPath && !visitedRight && avoid5?.hasPlayer(enemyPlayerPositions) != true && avoid7?.hasPlayer(enemyPlayerPositions) != true)
                    actions.add(Action.RIGHT)
                if (down == hasPath && !visitedDown && avoid8?.hasPlayer(enemyPlayerPositions) != true && avoid6?.hasPlayer(enemyPlayerPositions) != true && avoid7?.hasPlayer(enemyPlayerPositions) != true)
                    actions.add(Action.DOWN)
                if (left == hasPath && !visitedLeft && avoid4?.hasPlayer(enemyPlayerPositions) != true && avoid2?.hasPlayer(enemyPlayerPositions) != true && avoid3?.hasPlayer(enemyPlayerPositions) != true && avoid6?.hasPlayer(enemyPlayerPositions) != true)
                    actions.add(Action.LEFT)
            }

// we've visited all possible paths, so we go to a random place next
            if (actions.isEmpty()) {
                if (up == hasPath)
                    actions.add(Action.UP)
                if (right == hasPath)
                    actions.add(Action.RIGHT)
                if (down == hasPath)
                    actions.add(Action.DOWN)
                if (left == hasPath)
                    actions.add(Action.LEFT)
            }

            System.err.println("[w=$width,h=$height] ${if (!input5.isNullOrEmpty()) "input5=$input5 " else ""}${if (!input7.isNullOrEmpty()) "input7=$input7 " else ""}top: $up right: $right down: $down left= $left")
            System.err.println("available paths: ${if (up == hasPath) "up ${formatVisited(visitedUp)} " else ""}${if (right == hasPath) "right ${formatVisited(visitedRight)} " else ""}${if (down == hasPath) "down ${formatVisited(visitedDown)} " else ""}${if (left == hasPath) "left ${formatVisited(visitedLeft)} " else ""}")
            System.err.println("available actions: ${actions}")
// enemyPlayerPositions.forEachIndexed { index, pair -> System.err.println("${player[index]} $pair") }
            val action = actions.random(Random())
            System.err.println("${player[amountPlayer - 1]} ${playerPositions.last()} visited: ${playerMap[x][y]?.next(action, map)?.visited}")
            System.err.println("move=$action(${action?.value})")
            action?.move()
        }
    }

    lateinit var map: Array<Array<Tile?>>

    lateinit var playerMap: Array<Array<Tile?>>

    val hasPath = "_"
    val wall = "#"
    val unkown = "?"

    val player = listOf("A", "B", "C", "D", "X")

    data class Tile(
            val x: Int,
            val y: Int,
            var value: String = "$hasPath ",
            var visited: Boolean = false
    ) {
        fun up(map: Array<Array<Tile?>> = playerMap): Tile? = if (inRange(x, y - 1, map)) map[x][y - 1] else null
        fun right(map: Array<Array<Tile?>> = playerMap): Tile? = if (inRange(x + 1, y, map)) map[x + 1][y] else null
        fun down(map: Array<Array<Tile?>> = playerMap): Tile? = if (inRange(x, y + 1, map)) map[x][y + 1] else null
        fun left(map: Array<Array<Tile?>> = playerMap): Tile? = if (inRange(x - 1, y, map)) map[x - 1][y] else null
        fun inRange(x: Int, y: Int, map: Array<Array<Tile?>> = playerMap): Boolean = x > 0 && x < map.size && y > 0 && y < map[0].size

        fun next(action: Action?, map: Array<Array<Tile?>> = playerMap): Tile? = when (action) {
            Action.RIGHT -> right(map)
            Action.STAY -> this
            Action.UP -> up(map)
            Action.DOWN -> down(map)
            Action.LEFT -> left(map)
            else -> null
        }

        fun hasPlayer(playerPositions: List<Pair<Int, Int>>): Boolean = playerPositions.any { it.first == x && it.second == y }
    }

    /*
    C
    E B A
    D
    */
    enum class Action(val value: String) {
        RIGHT("A"),
        STAY("B"),
        UP("C"),
        DOWN("D"),
        LEFT("E");

        fun move() = println(value)
    }

    fun formatVisited(hasVisited: Boolean): String = if (hasVisited) "visited" else ""

    fun Array<Array<Tile?>>.copy() = Array(size) { get(it).clone() }

    fun <E> List<E>.random(random: java.util.Random): E? = if (size > 0) get(random.nextInt(size)) else null
    fun <E> Set<E>.random(random: java.util.Random): E? = if (size > 0) elementAt(random.nextInt(size)) else null
}