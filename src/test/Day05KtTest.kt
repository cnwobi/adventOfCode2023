import day5.Range
import day5.RangedState
import day5.rangedStateToNextStates
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
}
