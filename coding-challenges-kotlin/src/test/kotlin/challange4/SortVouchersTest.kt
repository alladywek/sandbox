package challange4

import challange4.Result.Error
import challange4.Result.Success
import challange4.VouchersDataAggregator.CsvToVouchersData
import challange4.VouchersDataAggregator.VouchersData
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.aggregator.AggregateWith
import org.junit.jupiter.params.aggregator.ArgumentsAccessor
import org.junit.jupiter.params.aggregator.ArgumentsAggregator
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo


class SortVouchersTest {

    @ParameterizedTest(name = "sortVouchers() returns the same result as input if input data is correct")
    @CsvSource(delimiter = '|', value = [
        "190111:Activated:ffff,190111:Available:cccc,190112:Activated:bbbb,190112:Available:aaaa",
        "190111:Available:cccc"
    ])
    fun test1(@CsvToVouchersData data: VouchersData) {
        val result = sortVouchers(data.input)
        expectThat(result)
                .isA<Success<String>>()
                .get { value }
                .isEqualTo(data.input)
    }

    @ParameterizedTest(name = "sortVouchers() returns validation error if input has no data")
    @ValueSource(strings = ["", " ", "   ", "\t", "\n"])
    fun test2(input: String) {
        val result = sortVouchers(input)
        expectThat(result)
                .isA<Error<ValidationError>>()
                .get { value }
                .isA<ValidationError.VouchersStringIsBlankOrEmpty>()
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
    fun test3(@CsvToVouchersData data: VouchersData) {
        val result = sortVouchers(data.input)
        expectThat(result)
                .isA<Success<String>>()
                .get { value }
                .isEqualTo(data.expected)
    }

    @ParameterizedTest(name = "sortVouchers() sorts vouchers by endDate descending with Redeemed or Expired statuses")
    @CsvSource(delimiter = '|', value = [
        "190110:Redeemed:aaaa,190111:Redeemed:bbbb                          | 190111:Redeemed:bbbb,190110:Redeemed:aaaa",
        "190111:Redeemed:aaaa,190110:Redeemed:bbbb                          | 190111:Redeemed:aaaa,190110:Redeemed:bbbb",
        "190108:Redeemed:aaaa,190111:Redeemed:bbbb,190110:Redeemed:cccc     | 190111:Redeemed:bbbb,190110:Redeemed:cccc,190108:Redeemed:aaaa",
        "190108:Redeemed:aaaa,190111:Expired:bbbb,190110:Expired:cccc       | 190111:Expired:bbbb,190110:Expired:cccc,190108:Redeemed:aaaa"
    ])
    fun test5(@CsvToVouchersData data: VouchersData) {
        val result = sortVouchers(data.input)
        expectThat(result)
                .isA<Success<String>>()
                .get { value }
                .isEqualTo(data.expected)
    }
}

class VouchersDataAggregator : ArgumentsAggregator {
    override fun aggregateArguments(arguments: ArgumentsAccessor, context: ParameterContext): VouchersData {
        val expected = if (arguments.size() > 1) arguments.getString(1) else null
        return VouchersData(arguments.getString(0), expected)
    }

    data class VouchersData(val input: String, val expected: String?)

    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.VALUE_PARAMETER)
    @AggregateWith(VouchersDataAggregator::class)
    annotation class CsvToVouchersData
}

