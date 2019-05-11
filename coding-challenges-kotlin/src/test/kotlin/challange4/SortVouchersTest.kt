package challange4

import challange4.Result.Error
import challange4.Result.Success
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

class SortVouchersTest {

    @ParameterizedTest
    @ValueSource(strings = [
        "190111:Activated:ffff,190111:Available:cccc,190112:Activated:bbbb,190112:Available:aaaa",
        "190111:Activated:ffff,190111:Available:cccc,190112:Activated:bbbb,190112:Available:aaaa,190110:Redeemed:dddd,190110:Expired:eeee",
        "190111:Available:cccc"
    ])
    @DisplayName("sortVouchers() returns the same result as input if input data is correct")
    fun test1(input: String) {
        val result = sortVouchers(input)
        expectThat(result)
                .isA<Success<String>>()
                .get { value }
                .isEqualTo(input)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "   ", "\t", "\n"])
    @DisplayName("sortVouchers() returns validation error if input has no data")
    fun test2(input: String) {
        val result = sortVouchers(input)
        expectThat(result)
                .isA<Error<ValidationError>>()
                .get { value }
                .isA<ValidationError.VouchersStringIsBlankOrEmpty>()
    }
}