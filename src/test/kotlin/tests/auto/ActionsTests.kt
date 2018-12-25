package tests.auto

import org.junit.jupiter.api.Test

class ActionsTests {

    @Test
    fun testNothingAction() {
        // turns out I cant test these because WPI made it impossible to use timer
        // val action: Action = NothingAction(1.0)
        // val startTime = System.currentTimeMillis()
        // action.start()
        // while(true) {
        //     if(action.next()) break
        // }
        // val duration = System.currentTimeMillis() - startTime
        // println("Duration: $duration ms")
        // assertEquals(duration, 1000.toLong())
    }
}
