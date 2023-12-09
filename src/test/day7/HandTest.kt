package day7

import main.aoc2023.day7.Hand
import main.aoc2023.day7.Type
import org.junit.jupiter.api.Test

import kotlin.test.expect

class HandTest {

    @Test
    fun rank2() {
        expect(Type.FIVE_OF_KIND) { Hand(listOf("J", "J", "J", "J", "J")).valueWithWildCard() }
        expect(Type.FIVE_OF_KIND) { Hand(listOf("A", "A", "A", "A", "A")).valueWithWildCard() }

        expect(Type.FIVE_OF_KIND) { Hand(listOf("J", "J", "J", "J", "2")).valueWithWildCard() }
        expect(Type.FIVE_OF_KIND) { Hand(listOf("J", "J", "J", "2", "2")).valueWithWildCard() }
        expect(Type.FOUR_OF_KIND) { Hand(listOf("J", "J", "J", "1", "2")).valueWithWildCard() }


        expect(Type.THREE_OF_KIND) { Hand(listOf("J", "J", "3", "1", "2")).valueWithWildCard() }
        expect(Type.FOUR_OF_KIND) { Hand(listOf("J", "J", "3", "3", "2")).valueWithWildCard() }
        expect(Type.FIVE_OF_KIND) { Hand(listOf("J", "J", "3", "3", "3")).valueWithWildCard() }



        expect(Type.ONE_PAIR) { Hand(listOf("J", "1", "2", "3", "4")).valueWithWildCard() }
        expect(Type.THREE_OF_KIND) { Hand(listOf("J", "1", "1", "3", "4")).valueWithWildCard() }
        expect(Type.FULL_HOUSE) { Hand(listOf("J", "1", "1", "3", "3")).valueWithWildCard() }
        expect(Type.FOUR_OF_KIND) { Hand(listOf("J", "1", "1", "1", "4")).valueWithWildCard() }
        expect(Type.FIVE_OF_KIND) { Hand(listOf("J", "1", "1", "1", "1")).valueWithWildCard() }
    }
}
