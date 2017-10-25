```kotlin
fun main(args: Array<String>) {

    basic {
        1 REM "This is BASIK - BASIC interpreter written in Kotlin"
        10 PRINT "Hello World!"
        20 FOR I(1 TO 5)
        30 PRINT { "Iteration: " + I }
        40 NEXT I
        60 LET { A = 1 }
        50 IF (2 > 7) GOTO 100
        70 INPUT { "Enter your name: " > C }
        80 PRINT { "Hello, " + C + "!" }
        90 LET { A = "BASIC is cool, right?" }
        100 PRINT { A }
    }

}
```
