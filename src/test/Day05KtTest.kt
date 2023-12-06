import day5.Range
import day5.RangedState
import day5.rangedStateToNextStates
import main.println
import kotlin.test.Test
import kotlin.test.expect


class Day05KtTest {

    @Test
    fun stateTransitionWhenOutRange() {

        val rangedState = RangedState(start = 10, range = 5)
        rangedState.end.println()
        val range = listOf(Range(start = 15, destinationStart = 20, range = 6))
        expect(expected = setOf(rangedState.copy(name = "soil-to-fertilizer")), message = "To return same") { rangedStateToNextStates(rangedState, range) }
    }

    @Test
    fun stateTransitionWhenStartsBeforeButPartOfRange() {

        val rangedState = RangedState(start = 20, range = 11)
        rangedState.end.println()
        val range = listOf(Range(start = 25, destinationStart = 40, range = 21))
        val expected = setOf(RangedState(name="soil-to-fertilizer", start=20, range=5), RangedState(name="soil-to-fertilizer", start=40, range=6))
        expect(expected = expected, message = "To return same") { rangedStateToNextStates(rangedState, range) }
    }

    @Test
    fun stateTransitionWhenStateCoversRange() {
        val  state = RangedState(start = 20, range = 31)
        val range = listOf(Range(start = 25, destinationStart = 20, range = 21))
        val expected = setOf(RangedState(name="soil-to-fertilizer", start=20, range=5), RangedState(name="soil-to-fertilizer", start=40, range=6))
        expect(expected) { rangedStateToNextStates(state, range) }
    }
}
