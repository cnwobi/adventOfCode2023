import day5.Range
import day5.RangedState
import day5.rangedStateToNextStates
import main.println
import kotlin.test.Test
import kotlin.test.expect


class Day05KtTest {

    @Test
    fun stateTransitionWhenOutRange() {

        val rangedState =  RangedState(start = 10, range = 5)
        val range = listOf( Range("seed-to-soil", start = 15, destinationStart = 20, range = 6))
        rangedStateToNextStates(rangedState, range)
        expect(expected = setOf(rangedState.copy(name = "soil-to-fertilizer")), message = "To return same" ){ rangedStateToNextStates(rangedState,range) }
    }

    @Test
    fun stateTransitionWhenOutRange2() {
        val rangedState =  RangedState(start = 5, range = 21)
        val range = listOf( Range("seed-to-soil", start = 15, destinationStart = 20, range = 6))
        rangedStateToNextStates(rangedState, range)
        expect(expected = setOf(rangedState.copy(name = "soil-to-fertilizer")), message = "To return same" ){ rangedStateToNextStates(rangedState,range) }
    }

    @Test
    fun stateTransistionWhenStateStartsAfterRangeStartButEndBefore() {
        val rangedState =  RangedState(start = 15, range = 21)
        val range = listOf( Range("seed-to-soil", start = 10, destinationStart = 20, range = 21))
        val expectedState1 = RangedState(name="soil-to-fertilizer", start=25, range=16)
        val expectedRangedState = RangedState(name="soil-to-fertilizer", start=31, range=5)
        expect(expected = setOf(expectedState1,expectedRangedState), message = "To return same" ){ rangedStateToNextStates(rangedState,range) }
    }

    @Test
    fun stateTransistionWhenStateStartsAfterRangeStartButEndBefore1() {
        val rangedState =  RangedState(start = 10, range = 91)
        rangedState.println()
        rangedState.end.println()
        val range = listOf( Range("seed-to-soil", start = 40, destinationStart = 10, range = 11),Range("seed-to-soil", start = 60, destinationStart = 70, range = 21))
        val expectedState1 = RangedState(name="soil-to-fertilizer", start=25, range=16)
        val expectedRangedState = RangedState(name="soil-to-fertilizer", start=31, range=5)
        val actual = rangedStateToNextStates(rangedState,range)

        expect(expected = setOf(expectedState1,expectedRangedState), message = "To return same" ){ actual }
    }




    @Test
    fun stateTransistionWhenStateStartsAfterRangeStartButEndBefore2() {
        val rangedState =  RangedState(start = 40, range = 11)
        val range = listOf( Range("seed-to-soil", start = 40, destinationStart = 20, range = 11))
        val expectedState1 = RangedState(name="soil-to-fertilizer", start=20, range=11)
        expect(expected = setOf(expectedState1), message = "To return same" ){ rangedStateToNextStates(rangedState,range) }
    }

    @Test
    fun stateTransistionWhenStateStartsAfterRangeStartButEndBefore3() {
        val rangedState =  RangedState(start = 40, range = 11)
        val range = listOf( Range("seed-to-soil", start = 50, destinationStart = 20, range = 11))
        val expectedState1 = RangedState(name="soil-to-fertilizer", start=20, range=11)
        expect(expected = setOf(expectedState1), message = "To return same" ){ rangedStateToNextStates(rangedState,range) }
    }
}
