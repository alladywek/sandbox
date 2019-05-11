package challenge4

fun sortVouchers(vouchers: String): Result<ValidationError, String> {
    if (vouchers.isBlank())
        return Result.Error(ValidationError.VouchersStringIsBlankOrEmpty())
    val (current, other) = vouchers
            .split(",")
            .map(String::buildVoucherFromString)
            .partition { it.status.current }
    val sortedCurrent = current.sortedWith(compareBy({ it.endDate }, { it.status }, { it.id }))
    val sortedOther = other.sortedWith(
            compareByDescending<Voucher> { it.endDate }.thenComparing(compareBy({ it.status }, { it.id }))
    )
    return Result.Success((sortedCurrent + sortedOther).joinToString(","))
}

private fun String.buildVoucherFromString(): Voucher {
    val (date, status, id) = this.split(":")
    return Voucher(date.toLong(), VoucherStatus.valueOf(status), id)
}

private class Voucher(val endDate: Long, val status: VoucherStatus, val id: String) {
    override fun toString(): String = "$endDate:$status:$id"
}

private enum class VoucherStatus(val current: Boolean) {
    Activated(true),
    Available(true),
    Redeemed(false),
    Expired(false),
}

sealed class Result<out E, out V> {
    data class Success<out V>(val value: V) : Result<Nothing, V>()
    data class Error<out E>(val value: E) : Result<E, Nothing>()
}

sealed class ValidationError : Exception() {
    class VouchersStringIsBlankOrEmpty : ValidationError()
}