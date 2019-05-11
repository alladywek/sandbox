package challange4

fun sortVouchers(vouchers: String): Result<ValidationError, String> {
    if (vouchers.isBlank())
        return Result.Error(ValidationError.VouchersStringIsBlankOrEmpty())
    val (current, notCurrent) = vouchers
            .split(",")
            .map(String::buildVoucherFromString)
            .partition { it.status.current }
    val sortedCurrent = current.sortedWith(compareBy(
            { it.endDate },
            { it.status }
    )).joinToString(",")
    val sortedNotCurrent = notCurrent.sortedWith(
            compareByDescending<Voucher> { it.endDate }
                    .thenBy { it.status }
    ).joinToString(",")

    return Result.Success("$sortedCurrent$sortedNotCurrent")
}

private fun String.buildVoucherFromString(): Voucher {
    val (date, status, id) = this.split(":")
    return Voucher(date.toLong(), Status.valueOf(status), id)
}

private class Voucher(val endDate: Long, val status: Status, val id: String) {
    override fun toString(): String = "$endDate:$status:$id"
}

enum class Status(val current: Boolean) {
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