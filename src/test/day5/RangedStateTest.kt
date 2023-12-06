package day5

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.test.expect

class RangedStateTest {

    @Test
    fun findIntersectionWhenStateIsBefore() {
        val rangedState = RangedState(start = 5, range = 6)
        val range = Range(destinationStart = 0, start = 20, range = 11)
        val expected = RangedState(name = "soil-to-fertilizer", start = 5, range = 6)
        expect(Triple(expected, null, null)) { rangedState.findIntersection(range) }
    }

    @Test
    fun findIntersectionWhenStateIsBeforeButIntersects() {
        val rangedState = RangedState(start = 10, range = 11)
        val range = Range(destinationStart = 20, start = 15, range = 36)
        val expected = RangedState(name = "soil-to-fertilizer", start = 10, range = 5)
        val expected2 = RangedState(name = "soil-to-fertilizer", start = 20, range = 6)
        expect(Triple(expected, expected2,null)) { rangedState.findIntersection(range) }
    }

    @Test
    fun findIntersectionWhenStateAligns() {
        val rangedState = RangedState(start = 10, range = 11)
        val range = Range(destinationStart = 50, start = 10, range = 11)
        val expected = RangedState(name = "soil-to-fertilizer", start = 50, range = 11)
        expect(Triple(null, expected, null)) { rangedState.findIntersection(range) }
    }

    @Test
    fun findInterSectionWhenStateStartsAfterRange() {
        val rangedState = RangedState(start = 15, range = 16)
        val range = Range(destinationStart = 0, start = 10, range = 11)
        val expected2 = RangedState(name = "soil-to-fertilizer", start = 5, range = 6)
        val expected3 = RangedState(name = "seed-to-soil", start = 21, range = 10)
        expect(Triple(null, expected2, expected3)) { rangedState.findIntersection(range) }

    }

    @Test
    fun findInterSectionWhenStateIsOutRange() {
        val rangedState = RangedState(start = 31, range = 11)
        val range = Range(destinationStart = 0, start = 15, range = 16)
        val expected3 = RangedState(name = "seed-to-soil", start = 31, range = 11)
        expect(Triple(null, null, expected3)) { rangedState.findIntersection(range) }
    }

    @Test
    fun findIntersectionWhenStateIsSubset() {
        val state = RangedState(start = 10, range = 11)
        val range = Range(destinationStart = 0, start = 0, range = 41)
        val expected2 = RangedState(name = "soil-to-fertilizer", start = 10, range = 11)
        expect(Triple(null, expected2, null)) { state.findIntersection(range) }
    }
}
