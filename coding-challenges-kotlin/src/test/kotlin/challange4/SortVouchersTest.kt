package challange4

import challange4.Result.Success
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

class SortVouchersTest {

    @Test
    @DisplayName("Expect that sortVouchers() returns the same result as input if input data is correct")
    fun test1() {
        val input = "input"
        val result = sortVouchers(input)
        expectThat(result)
                .isA<Success<String>>()
                .get { value }
                .isEqualTo(input)
    }
}