package day7

import main.day7.CamelCard
import main.day7.HandType
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.test.expect

class CamelCardTest {

    @Test
    fun rank2() {
        expect(HandType.FIVE_OF_KIND) { CamelCard(listOf("J", "J", "J", "J", "J")).rank2() }
        expect(HandType.FIVE_OF_KIND) { CamelCard(listOf("A", "A", "A", "A", "A")).rank2() }

        expect(HandType.FIVE_OF_KIND) { CamelCard(listOf("J", "J", "J", "J", "2")).rank2() }
        expect(HandType.FIVE_OF_KIND) { CamelCard(listOf("J", "J", "J", "2", "2")).rank2() }
        expect(HandType.FOUR_OF_KIND) { CamelCard(listOf("J", "J", "J", "1", "2")).rank2() }


        expect(HandType.THREE_OF_KIND) { CamelCard(listOf("J", "J", "3", "1", "2")).rank2() }
        expect(HandType.FOUR_OF_KIND) { CamelCard(listOf("J", "J", "3", "3", "2")).rank2() }
        expect(HandType.FIVE_OF_KIND) { CamelCard(listOf("J", "J", "3", "3", "3")).rank2() }



        expect(HandType.ONE_PAIR) { CamelCard(listOf("J", "1", "2", "3", "4")).rank2() }
        expect(HandType.THREE_OF_KIND) { CamelCard(listOf("J", "1", "1", "3", "4")).rank2() }
        expect(HandType.FULL_HOUSE) { CamelCard(listOf("J", "1", "1", "3", "3")).rank2() }
        expect(HandType.FOUR_OF_KIND) { CamelCard(listOf("J", "1", "1", "1", "4")).rank2() }
        expect(HandType.FIVE_OF_KIND) { CamelCard(listOf("J", "1", "1", "1", "1")).rank2() }
    }
}
