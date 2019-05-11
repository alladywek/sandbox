package challenge4

import challenge4.TestDataAggregator.CsvToTestData
import challenge4.TestDataAggregator.TestData
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.aggregator.AggregateWith
import org.junit.jupiter.params.aggregator.ArgumentsAccessor
import org.junit.jupiter.params.aggregator.ArgumentsAggregator
import org.junit.jupiter.params.provider.CsvSource
import strikt.api.expectThat
import strikt.assertions.isEqualTo


class SortVouchersTest {

    @ParameterizedTest(name = "sortVouchers() returns the same result as input if input data is correct")
    @CsvSource(delimiter = '|', value = [
        "190111:Activated:ffff,190111:Available:cccc,190112:Activated:bbbb,190112:Available:aaaa",
        "190111:Available:cccc"
    ])
    fun test1(@CsvToTestData data: TestData) {
        val result = sortVouchers(data.input)
        expectThat(result).isEqualTo(data.input)
    }

    @ParameterizedTest(name = "sortVouchers() sorts vouchers by endDate ascending with Activated or Available statuses")
    @CsvSource(delimiter = '|', value = [
        "190111:Activated:aaaa,190110:Activated:bbbb                                                | 190110:Activated:bbbb,190111:Activated:aaaa",
        "200101:Activated:aaaa,191231:Activated:bbbb                                                | 191231:Activated:bbbb,200101:Activated:aaaa",
        "191010:Activated:aaaa,191009:Activated:bbbb,190908:Activated:cccc                          | 190908:Activated:cccc,191009:Activated:bbbb,191010:Activated:aaaa",
        "190110:Available:aaaa,190110:Activated:bbbb                                                | 190110:Activated:bbbb,190110:Available:aaaa",
        "190109:Available:aaaa,190110:Activated:bbbb                                                | 190109:Available:aaaa,190110:Activated:bbbb",
        "190109:Available:aaaa,190110:Available:bbbb,190110:Activated:cccc,190109:Activated:dddd    | 190109:Activated:dddd,190109:Available:aaaa,190110:Activated:cccc,190110:Available:bbbb"
    ])
    fun test3(@CsvToTestData data: TestData) {
        val result = sortVouchers(data.input)
        expectThat(result).isEqualTo(data.expected)
    }

    @ParameterizedTest(name = "sortVouchers() sorts vouchers by endDate descending with Redeemed or Expired statuses")
    @CsvSource(delimiter = '|', value = [
        "190110:Redeemed:aaaa,190111:Redeemed:bbbb                          | 190111:Redeemed:bbbb,190110:Redeemed:aaaa",
        "190111:Redeemed:aaaa,190110:Redeemed:bbbb                          | 190111:Redeemed:aaaa,190110:Redeemed:bbbb",
        "190108:Redeemed:aaaa,190111:Redeemed:bbbb,190110:Redeemed:cccc     | 190111:Redeemed:bbbb,190110:Redeemed:cccc,190108:Redeemed:aaaa",
        "190108:Redeemed:aaaa,190111:Expired:bbbb,190110:Expired:cccc       | 190111:Expired:bbbb,190110:Expired:cccc,190108:Redeemed:aaaa"
    ])
    fun test5(@CsvToTestData data: TestData) {
        val result = sortVouchers(data.input)
        expectThat(result).isEqualTo(data.expected)
    }

    @ParameterizedTest(name = "sortVouchers() sorts vouchers by all conditions")
    @CsvSource(delimiter = '|', value = [
        "190112:Available:aaaa,190112:Activated:bbbb,190111:Available:cccc,190110:Redeemed:dddd,190110:Expired:eeee,190111:Activated:ffff       | 190111:Activated:ffff,190111:Available:cccc,190112:Activated:bbbb,190112:Available:aaaa,190110:Redeemed:dddd,190110:Expired:eeee",
        "190112:Available:aaaa,190112:Activated:bbbb,190111:Available:cccc,190110:Redeemed:dddd,190110:Redeemed:aaaa,190111:Activated:ffff,190111:Activated:aabb     | 190111:Activated:aabb,190111:Activated:ffff,190111:Available:cccc,190112:Activated:bbbb,190112:Available:aaaa,190110:Redeemed:aaaa,190110:Redeemed:dddd"
    ])
    fun test6(@CsvToTestData data: TestData) {
        val result = sortVouchers(data.input)
        expectThat(result).isEqualTo(data.expected)
    }
}

class TestDataAggregator : ArgumentsAggregator {
    override fun aggregateArguments(arguments: ArgumentsAccessor, context: ParameterContext): TestData {
        val expected = if (arguments.size() > 1) arguments.getString(1) else null
        return TestData(arguments.getString(0), expected)
    }

    data class TestData(val input: String, val expected: String?)

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.VALUE_PARAMETER)
    @AggregateWith(TestDataAggregator::class)
    annotation class CsvToTestData
}