package net.kibotu.pacman

import java.util.*


// region https://www.codingame.com/ide/puzzle/codingame-sponsored-contest
// https://github.com/niconoe-/Codingame/blob/master/1%20-%20Puzzles/02%20-%20Optimization%20Puzzles/CodinGame%20Sponsored%20Challenge/PHP.php
// behavior tree https://www.javacodegeeks.com/2014/08/game-ai-an-introduction-to-behaviour-trees.html

fun main(args: Array<String>) {

    val input = Scanner(System.`in`)

    val game = Game(input)

    var turn = 0
    while (true) {
        game.update(input, turn)
        turn++
    }
}

class Game(
        var board: Board,
        var player: List<Player>,
        var input4: String
) {
    constructor(input: Scanner) : this(Board(input), List(input.nextInt()) { Player() }, if (input.hasNextLine()) {
        input.nextLine()
    } else "")

    var input7: String = ""

    fun update(input: Scanner, turn: Int) {

        val vision = Vision(input)

        player.forEachIndexed { index, player ->
            player.name = "$index"
            player.set(input)
        }

        board.update(player, vision)

        // unknown parameter
        input7 = input.nextLine()

        // decide open paths
        // decide based

        board.print()

        System.err.println("[w=${board.width},h=${board.height}] ${if (!input4.isEmpty()) "input4=$input4 " else ""}${if (!input7.isNullOrEmpty()) "input7=$input7 " else ""}up: ${vision.up} right: ${vision.right} down: ${vision.down} left= ${vision.left}")

        val behaviour = Move()
        behaviour.start()
        behaviour.act(player.last(), board)
    }
}

class Player(var x: Int = 0, var y: Int = 0) {

    var name: String = ""

    fun set(input: Scanner) {
        x = input.nextInt()
        y = input.nextInt()
    }

    override fun toString(): String = name
}

class Board(
        var width: Int,
        var height: Int
) {
    constructor(input: Scanner) : this(input.nextInt() + 1, input.nextInt() + 1)

    var map: Array<Array<Tile?>> = Array(width) { arrayOfNulls<Tile>(height) }

    init {

        for (i in 0 until map.size) {
            for (j in 0 until map[i].size) {
                map[i][j] = Tile(i, j)
            }
        }
    }

    fun update(players: List<Player>, vision: Vision) {

        clearPlayer()

        players.forEach {
            tile(it)?.apply {
                isPath = true
                player = it
            }
        }

        applyVision(players.last(), vision)
    }

    private fun applyVision(player: Player, vision: Vision) = tile(player)?.apply {
        up(map)?.update(vision.up)
        right(map)?.update(vision.right)
        down(map)?.update(vision.down)
        left(map)?.update(vision.left)
    }

    private fun clearPlayer() {
        for (j in 0 until map[0].size) {
            for (i in 0 until map.size) {
                map[i][j]?.player = null
            }
        }
    }

    fun tile(player: Player): Tile? = map[player.x][player.y]

    fun print() {
        for (j in 0 until map[0].size) {
            for (i in 0 until map.size) {
                System.err.print(map[i][j].toString())
            }
            System.err.println()
        }
    }
}

data class Tile(
        val x: Int,
        val y: Int,
        var player: Player? = null,
        var visited: Boolean = false,
        var isWall: Boolean = false,
        var isPath: Boolean = false,
        var isUnknown: Boolean = false
) {
    fun up(map: Array<Array<Tile?>>): Tile? = if (inRange(x, y - 1, map)) map[x][y - 1] else null

    fun right(map: Array<Array<Tile?>>): Tile? = if (inRange(x + 1, y, map)) map[x + 1][y] else null

    fun down(map: Array<Array<Tile?>>): Tile? = if (inRange(x, y + 1, map)) map[x][y + 1] else null

    fun left(map: Array<Array<Tile?>>): Tile? = if (inRange(x - 1, y, map)) map[x - 1][y] else null

    private fun inRange(x: Int, y: Int, map: Array<Array<Tile?>>): Boolean = x > 0 && x < map.size && y > 0 && y < map[0].size

    fun next(action: Action?, map: Array<Array<Tile?>>): Tile? = when (action) {
        Action.RIGHT -> right(map)
        Action.STAY -> this
        Action.UP -> up(map)
        Action.DOWN -> down(map)
        Action.LEFT -> left(map)
        else -> null
    }

    fun hasPlayer(playerPositions: List<Pair<Int, Int>>): Boolean = playerPositions.any { it.first == x && it.second == y }

    fun update(vision: String) {
        when (vision) {
            path -> isPath = true
            wall -> isWall = true
            unknown -> isUnknown = true
        }
    }

    fun canGoUp(board: Board): Boolean = up(board.map)?.isPath == true

    fun canGoRight(board: Board): Boolean = right(board.map)?.isPath == true

    fun canGoDown(board: Board): Boolean = down(board.map)?.isPath == true

    fun canGoLeft(board: Board): Boolean = left(board.map)?.isPath == true

    override fun toString(): String = when {
        player != null -> "$player "
        isWall -> "$wall "
        isPath -> "$path "
        isUnknown -> "$unknown "
        else -> "$wall "
    }
}

val path = "_"
val wall = "#"
val unknown = "?"

class Vision(var up: String, var right: String, var down: String, var left: String) {
    constructor(input: Scanner) : this(input.nextLine(), input.nextLine(), input.nextLine(), input.nextLine())
}


class Move : Behaviour() {

    override fun reset() = start()

    override fun act(player: Player, board: Board) {

        val tile = board.tile(player)
        val up = tile?.up(board.map)
        val right = tile?.right(board.map)
        val down = tile?.down(board.map)
        val left = tile?.left(board.map)

        System.err.println("available paths: ${if (up?.isPath == true) "up ${up.visited} " else ""}${if (right?.isPath == true) "right ${right.visited} " else ""}${if (down?.isPath == true) "down ${down.visited} " else ""}${if (left?.isPath == true) "left ${left.visited} " else ""}")

        var behaviour: Behaviour = MoveUp()
        behaviour.start()
        behaviour.act(player, board)
        if (behaviour.isSuccess)
            return

        behaviour = MoveRight()
        behaviour.start()
        behaviour.act(player, board)
        if (behaviour.isSuccess)
            return

        behaviour = MoveDown()
        behaviour.start()
        behaviour.act(player, board)
        if (behaviour.isSuccess)
            return

        behaviour = MoveLeft()
        behaviour.start()
        behaviour.act(player, board)
        if (behaviour.isSuccess)
            return

        behaviour = Stay()
        behaviour.start()
        behaviour.act(player, board)
        if (behaviour.isSuccess)
            return
    }
}

class MoveUp : Behaviour() {

    override fun reset() = start()

    override fun act(player: Player, board: Board) {
        if (!isRunning) return

        val tile = board.tile(player)

        if (tile?.canGoUp(board) == false) {
            fail()
            return
        }

        Action.UP.move()

        succeed()
    }
}

class MoveRight : Behaviour() {

    override fun reset() = start()

    override fun act(player: Player, board: Board) {
        if (!isRunning) return

        val tile = board.tile(player)

        if (tile?.canGoRight(board) == false) {
            fail()
            return
        }

        Action.RIGHT.move()

        succeed()
    }
}

class MoveDown : Behaviour() {

    override fun reset() = start()

    override fun act(player: Player, board: Board) {
        if (!isRunning) return

        val tile = board.tile(player)

        if (tile?.canGoDown(board) == false) {
            fail()
            return
        }

        Action.DOWN.move()

        succeed()
    }
}

class MoveLeft : Behaviour() {

    override fun reset() = start()

    override fun act(player: Player, board: Board) {
        if (!isRunning) return

        val tile = board.tile(player)

        if (tile?.canGoLeft(board) == false) {
            fail()
            return
        }

        Action.LEFT.move()

        succeed()
    }
}

class Stay : Behaviour() {

    override fun reset() = start()

    override fun act(player: Player, board: Board) {
        if (!isRunning) return

        Action.STAY.move()

        succeed()
    }
}

class HasVisited : Behaviour() {

    override fun reset() {
        start()
    }

    override fun act(player: Player, board: Board) {

    }
}

abstract class Behaviour protected constructor() {

    enum class State {
        Success,
        Failure,
        Running
    }

    var state: State? = null
        protected set

    val isSuccess: Boolean
        get() = state == State.Success

    val isFailure: Boolean
        get() = state == State.Failure

    val isRunning: Boolean
        get() = state == State.Running

    fun start() {
        this.state = State.Running
    }

    abstract fun reset()

    abstract fun act(player: Player, board: Board)

    protected fun succeed() {
        this.state = State.Success
    }

    protected fun fail() {
        this.state = State.Failure
    }
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
