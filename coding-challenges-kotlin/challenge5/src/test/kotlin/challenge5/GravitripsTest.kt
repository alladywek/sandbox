package challenge5

import challenge5.TestDataAggregator.CsvToTestData
import challenge5.TestDataAggregator.TestData
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.aggregator.AggregateWith
import org.junit.jupiter.params.aggregator.ArgumentsAccessor
import org.junit.jupiter.params.aggregator.ArgumentsAggregator
import org.junit.jupiter.params.provider.CsvSource
import strikt.api.expectThat
import strikt.assertions.isEqualTo


class SortVouchersTest {

    @ParameterizedTest(name = "getGridStatus()")
    @CsvSource(delimiter = '|', value = [
        """
           '".......",
            ".......",
            ".R.....",
            ".r.....",
            ".ry....",
            ".ryyy.."' | 'Red wins'
        """
    ])
    fun test1(@CsvToTestData data: TestData) {
        val result = getGridStatus(data.inputFields)
        expectThat(result).isEqualTo(data.expectedStatus)
    }
}

object TestDataAggregator : ArgumentsAggregator {
    private val regex = "[.RYry]+".toRegex()

    override fun aggregateArguments(arguments: ArgumentsAccessor, context: ParameterContext): TestData {
        val fields = regex.findAll(arguments.getString(0)).map { it.value }.toList()
        val status = arguments.getString(1)
        return TestData(fields, status)
    }

    class TestData(val inputFields: List<String>, val expectedStatus: String)

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.VALUE_PARAMETER)
    @AggregateWith(TestDataAggregator::class)
    annotation class CsvToTestData
}