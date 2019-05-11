package challange4

fun sortVouchers(vouchers: String): Result<ValidationError, String> {
    if (vouchers.isBlank())
        return Result.Error(ValidationError.VouchersStringIsBlankOrEmpty())
    val sortedVouchers = vouchers
            .split(",")
            .map(String::buildVoucherFromString)
            .sortedWith(compareBy(
                    { it.endDate },
                    { it.status }
            ))
            .joinToString(",")
    return Result.Success(sortedVouchers)
}

private fun String.buildVoucherFromString(): Voucher {
    val (date, status, id) = this.split(":")
    return Voucher(date.toLong(), status, id)
}

private class Voucher(val endDate: Long, val status: String, val id: String) {
    override fun toString(): String = "$endDate:$status:$id"
}

sealed class Result<out E, out V> {
    data class Success<out V>(val value: V) : Result<Nothing, V>()
    data class Error<out E>(val value: E) : Result<E, Nothing>()
}

sealed class ValidationError : Exception() {
    class VouchersStringIsBlankOrEmpty : ValidationError()
}