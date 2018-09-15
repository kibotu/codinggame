package net.kibotu

import java.util.*

fun main(args: Array<String>) {
    Player.main(args)
}

// Class that interacts with Codingame
object Player {

    fun main(args: Array<String>) {
        val input = Scanner(System.`in`)
        var turn = 0
        val man = Manager()
        while (true) {
            man.update(input, turn)
            val command = man.command
            println(command.toString())
            turn++
        }
    }
}

// Class that makes turn decisions
class Manager {
    var myself: User? = null
    var opp: User? = null
    var turn: Int = 0
    var draftCards = ArrayList<Card>()
    val command: CommandGroup
        get() = if (turn < 30) {
            draftCommand
        } else {
            turnCommand
        }
    // CHOOSING DRAFT CARD
    // adding so can keep track
    // SUBMITTING COMMAND
    val draftCommand: CommandGroup
        get() {
            val toReturn = CommandGroup()
            var toDraft: Card? = null
            var bestScore = -10000000f
            for (i in draftCards.size - 3 until draftCards.size) {
                val score = myself!!.scoreDraftCard(draftCards[i])
                System.err.println(draftCards[i].toString() + " score:" + score)
                if (score > bestScore) {
                    toDraft = draftCards[i]
                    bestScore = score
                }
            }
            System.err.println("Drafting " + toDraft!!.id)
            Constants.draft.add(toDraft)
            toReturn.addCommand(Constants.DRAFT, toDraft)
            return toReturn
        }
    // SUMMONING LOGIC
    // ATTACK LOGIC
    // stay null if want to attack opponent
    val turnCommand: CommandGroup
        get() {
            val toReturn = CommandGroup()
            for (i in myself!!.creaturesInHand.indices) {
                if (myself!!.mana >= myself!!.creaturesInHand[i].cost) {
                    toReturn.addCommand(Constants.SUMMON, myself!!.creaturesInHand[i])
                    myself!!.increaseMana(-myself!!.creaturesInHand[i].cost)
                }
            }
            var creatureToAttack: Creature? = null
            for (i in opp!!.creaturesOnBoard.indices) {
                val opCreature = opp!!.creaturesOnBoard[i]
                if (opCreature.guard()) {
                    creatureToAttack = opCreature
                }
            }
            for (i in myself!!.creaturesOnBoard.indices) {
                toReturn.addCommand(Constants.ATTACK, myself!!.creaturesOnBoard[i], creatureToAttack)
            }
            return toReturn
        }

    init {
        myself = null
        opp = null
        turn = -1
    }

    fun update(input: Scanner, turn: Int) {
        myself = User(input)
        opp = User(input)
        this.turn = turn

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
            val newCard = Card(input, i)
            if (turn < 30) {
                newCard.id = i
                draftCards.add(newCard)
            } else {
                if (newCard.type == 0 && newCard.isMine) { // CREATURE
                    val newCreature = Creature(newCard)
                    myself!!.addCreature(newCreature)
                } else if (newCard.isMine) { // ITEM
                    val newItem = Item(newCard)
                    myself!!.addItem(newItem)
                } else { // OPPONENT
                    if (newCard.type == 0) {
                        val newCreature = Creature(newCard)
                        opp!!.addCreature(newCreature)
                    } else {
                        val newItem = Item(newCard)
                        opp!!.addItem(newItem)
                    }
                }
            }
        }
    }
}

class CommandGroup {
    var commands = ArrayList<Command>()
    var comment: String

    init {
        comment = ""
    }

    override fun toString(): String {
        if (commands.size == 0) {
            return "PASS"
        }
        var toReturn = ""
        for (i in commands.indices) {
            if (i > 0) {
                toReturn += ";"
            }
            toReturn += commands[i].toString()
        }
        return toReturn + comment
    }

    fun addCommand() {
        val c = Command()
        commands.add(c)
    }

    fun addCommand(type: Int) {
        val c = Command(type)
        commands.add(c)
    }

    fun addCommand(type: Int, a: Card) {
        val c = Command(type, a)
        commands.add(c)
    }

    fun addCommand(type: Int, a: Card, b: Card?) {
        val c = Command(type, a, b)
        commands.add(c)
    }

    fun addCommand(c: Command) {
        commands.add(c)
    }
}

class Command {
    var type: Int = 0
    var a: Card? = null
    var b: Card? = null
    var attackOp = false

    constructor() { // CONSTRUCTOR FOR PASS
        this.type = Constants.PASS
    }

    constructor(type: Int) {
        this.type = type
    }

    constructor(type: Int, a: Card) {
        this.type = type
        this.a = a
        if (this.type == Constants.ATTACK || this.type == Constants.USE) {
            attackOp = true
        }
    }

    constructor(type: Int, a: Card, b: Card? = null) {
        this.type = type
        this.a = a
        this.b = b
        if (this.b == null) { // null Card b means to attack opponent
            attackOp = true
        }
    }

    override fun toString(): String {
        var toReturn = ""
        when (type) {
            Constants.PASS -> {
                toReturn = "PASS"
                return toReturn
            }
            Constants.ATTACK -> toReturn = "ATTACK"
            Constants.USE -> toReturn = "USE"
            Constants.DRAFT -> {
                toReturn = "PICK " + a!!.id
                return toReturn
            }
            Constants.SUMMON -> toReturn = "SUMMON"
            else -> {
                toReturn = "PASS"
                return toReturn
            }
        }
        toReturn += " " + a!!.id
        if (b == null) {
            if (attackOp) {
                toReturn += " -1"
            }
        } else {
            toReturn += " " + b!!.id
        }
        return toReturn
    }
}

// Class that holds information for each "player" in the game
class User {
    var health: Int = 0
    var mana: Int = 0
    var deck: Int = 0
    var rune: Int = 0
    var draw: Int = 0
    var creaturesOnBoard = ArrayList<Creature>()
    var creaturesInHand = ArrayList<Creature>()
    var itemsOnBoard = ArrayList<Item>()
    var itemsInHand = ArrayList<Item>()

    constructor(health: Int, mana: Int, deck: Int, rune: Int, draw: Int) {
        this.health = health
        this.mana = mana
        this.deck = deck
        this.rune = rune
        this.draw = draw
    }

    constructor(input: Scanner) {
        this.health = input.nextInt()
        this.mana = input.nextInt()
        this.deck = input.nextInt()
        this.rune = input.nextInt()
        this.draw = input.nextInt()
    }

    fun addCreature(a: Creature) {
        if (Math.abs(a.location) == 1) {
            creaturesOnBoard.add(a)
        } else {
            creaturesInHand.add(a)
        }
    }

    fun addItem(a: Item) {
        if (Math.abs(a.location) == 1) {
            itemsOnBoard.add(a)
        } else {
            itemsInHand.add(a)
        }
    }

    fun increaseMana(mana: Int) {
        this.mana += mana
    }

    fun scoreDraftCard(b: Card): Float {
        val numDrafted = Constants.numDrafted
        val numItems = Constants.numItemsDrafted
        val numCreatures = Constants.numCreaturesDrafted
        val MAXITEMS = 5 // change this to change items to creatures ratio
        val MAXCREATURES = 30 - MAXITEMS
        if (b.isCreature) {
            val a = Creature(b)
            var score = a.draftScore
            if (numCreatures >= MAXCREATURES) {
                score -= 10000f
            }
            return score
        } else {
            val a = Item(b)
            var score = a.draftScore
            if (numItems >= MAXITEMS) {
                score -= 10000f
            }
            return score
        }
    }
}

// Class for a card
open class Card {
    var number: Int = 0
    var id: Int = 0
    var location: Int = 0
    var type: Int = 0
    var cost: Int = 0
    var attack: Int = 0
    var defense: Int = 0
    var myHealthChange: Int = 0
    var opponentHealthChange: Int = 0
    var cardDraw: Int = 0
    var draftPos: Int = 0
    var abilities: String
    val isInHand: Boolean
        get() = location == 0
    val isOnBoard: Boolean
        get() = location == 1
    val isCreature: Boolean
        get() = type == 0
    val isMine: Boolean
        get() = this.location >= 0

    constructor(number: Int, id: Int, location: Int, type: Int, cost: Int, attack: Int, defense: Int, abilities: String,
                myHealthChange: Int, opponentHealthChange: Int, cardDraw: Int) {
        this.number = number
        this.id = id
        this.location = location
        this.type = type
        this.cost = cost
        this.attack = attack
        this.defense = defense
        this.abilities = abilities
        this.myHealthChange = myHealthChange
        this.opponentHealthChange = opponentHealthChange
        this.cardDraw = cardDraw
    }

    constructor(input: Scanner, draftPos: Int) {
        this.number = input.nextInt()
        this.id = input.nextInt()
        this.location = input.nextInt()
        this.type = input.nextInt()
        this.cost = input.nextInt()
        this.attack = input.nextInt()
        this.defense = input.nextInt()
        this.abilities = input.next()
        this.myHealthChange = input.nextInt()
        this.opponentHealthChange = input.nextInt()
        this.cardDraw = input.nextInt()
        this.draftPos = draftPos
    }

    fun breakthrough(): Boolean {
        return this.abilities.contains("B")
    }

    fun charge(): Boolean {
        return this.abilities.contains("C")
    }

    fun drain(): Boolean {
        return this.abilities.contains("D")
    }

    fun guard(): Boolean {
        return this.abilities.contains("G")
    }

    fun lethal(): Boolean {
        return this.abilities.contains("L")
    }

    fun ward(): Boolean {
        return this.abilities.contains("W")
    }

    override fun toString(): String {
        return ("type:" + this.number + " id:" + this.id + " att:" + this.attack + " def:"
                + this.defense + "abil:" + this.abilities + " cost:" + this.cost)
    }
}

class Creature(a: Card) : Card(a.number, a.id, a.location, a.type, a.cost, a.attack, a.defense, a.abilities, a.myHealthChange, a.opponentHealthChange, a.cardDraw) {
    // abilities
    val draftScore: Float
        get() {
            var score = 0f
            score += (this.attack * 1.0).toFloat()
            score += (this.defense * 1.0).toFloat()
            score += (-this.cost * 2.0).toFloat()
            score += (this.cardDraw * 1.0).toFloat()
            score += (if (this.breakthrough()) 1 else 0).toFloat()
            score += (if (this.charge()) 1 else 0).toFloat()
            score += (if (this.drain()) 1 else 0).toFloat()
            score += (if (this.guard()) 2 else 0).toFloat()
            score += (if (this.lethal()) 1 else 0).toFloat()
            score += (if (this.ward()) 1 else 0).toFloat()
            return score
        }
}

class Item(a: Card) : Card(a.number, a.id, a.location, a.type, a.cost, a.attack, a.defense, a.abilities, a.myHealthChange, a.opponentHealthChange, a.cardDraw) {
    // abilities
    // devalues items over creatures
    val draftScore: Float
        get() {
            var score = 0f
            when( this.type) {
                Constants.GREEN -> {
                    score += (this.attack * 1.0).toFloat()
                    score += (this.defense * 1.0).toFloat()
                }
                Constants.RED -> {
                    score -= (this.attack * 1.0).toFloat()
                    score -= (this.defense * 1.0).toFloat()
                }
                 Constants.BLUE -> {
                    score -= (this.defense * 1.0).toFloat()
                    score += (this.myHealthChange * 1.0).toFloat()
                    score -= (this.opponentHealthChange * 1.0).toFloat()
                }
            }
            score += (-this.cost * 2.0).toFloat()
            score += (this.cardDraw * 1.0).toFloat()
            score += (if (this.breakthrough()) 1 else 0).toFloat()
            score += (if (this.charge()) 1 else 0).toFloat()
            score += (if (this.drain()) 1 else 0).toFloat()
            score += (if (this.guard()) 2 else 0).toFloat()
            score += (if (this.lethal()) 1 else 0).toFloat()
            score += (if (this.ward()) 1 else 0).toFloat()
            score *= 0.75f
            return score
        }

    init {
        type = a.type
    }
}

object Constants {
    // POSSIBLE MOVES
    const val PASS = 0
    const val SUMMON = 1
    const val ATTACK = 2
    const val USE = 3
    const val DRAFT = 4
    // ALL CARD TYPES
    const val CREATURE = 0
    const val GREEN = 1
    const val RED = 2
    const val BLUE = 3
    // DRAFTED CARDS
    var draft = ArrayList<Card>()
    val numDrafted: Int
        get() = draft.size
    val numItemsDrafted: Int
        get() = draft.indices.count { !draft[it].isCreature }
    val numCreaturesDrafted: Int
        get() = draft.indices.count { draft[it].isCreature }
}