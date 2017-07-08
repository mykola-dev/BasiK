package ds.basik

import java.util.Deque
import java.util.LinkedList
import java.util.Scanner

fun basic(code: MainContext.() -> Unit) {
    val context = MainContext()
    code(context)
    context.run()
}

typealias Statement = () -> Unit

class Variable<T : Any>(var value: T? = null) {

    override fun toString(): String = value.toString()

}

open class BasicContext {
    val program: MutableMap<Int, Statement> = mutableMapOf()
    var currLine = 1
    var maxLine = -1

    fun run() {
        if (maxLine < 0)
            maxLine = program.keys.max()!!.toInt()
        while (currLine <= maxLine) {
            currLine++
            program[currLine]?.invoke()
        }
    }
}

class MainContext : BasicContext() {

    // reserved for 'for' loops
    var I = Variable<Int>()
    var N = Variable<Int>()

    var A: Any? = null
    var B: Any? = null
    var C = Variable<String>()

    private val loops: Deque<ForLoop> = LinkedList()

    private fun getProgramStack(): MutableMap<Int, Statement> = loops.peek()?.program ?: program

    infix fun Int.REM(text: String) = Unit

    infix fun Int.LET(block: BasicContext.() -> Unit) = getProgramStack().put(this, { block(this@MainContext) })

    infix fun Int.PRINT(text: Any?) = getProgramStack().put(this, { println(text.toString()) })
    infix fun Int.PRINT(text: () -> Any?) = getProgramStack().put(this, { println(text().toString()) })

    infix fun Int.TO(end: Int): IntRange = this..end

    //operator fun Variable<Int>.invoke(range: IntRange): ForLoop = ForLoop(range, this)
    fun I(range: IntRange): ForLoop = ForLoop(range, I.apply { value = range.first })

    fun N(range: IntRange): ForLoop = ForLoop(range, N.apply { value = range.first })

    infix fun Int.FOR(loop: ForLoop): ForLoop {
        loop.startLine = this
        loops.push(loop)
        return loop
    }

    infix fun Int.NEXT(loopVar: Variable<Int>) {
        val loop = loops.pop()
        loop.maxLine = this
        getProgramStack().put(this) {
            with(loop) {
                for (i in range) {
                    currLine = startLine
                    run()
                    loopVar.run { value = value!! + 1 }
                }
            }
        }
    }

    infix fun Int.IF(condition: Boolean) = if (condition) this else -1

    infix fun Int.GOTO(line: Int) = getProgramStack().put(this) {
        currLine = line
        getProgramStack()[line]!!.invoke()
    }

    infix fun Int.INPUT(block: InputContext.() -> Boolean) = getProgramStack().put(this) {
        val ctx = InputContext()
        block(ctx)
        print(ctx.message)
        val s = Scanner(System.`in`)
        val text = s.nextLine()
        ctx.input.value = text
    }

}

class ForLoop(val range: IntRange, val loopVar: Variable<Int>) : BasicContext() {
    var startLine = -1
}

class InputContext {

    lateinit var input: Variable<String>
    lateinit var message: String

    operator fun String.compareTo(param: Variable<String>): Int {
        message = this
        input = param
        return 0
    }
}
