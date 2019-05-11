package challange4

fun sortVouchers(vouchers: String): Result<ValidationError, String> {
    if (vouchers.isBlank())
        return Result.Error(ValidationError.VouchersStringIsBlankOrEmpty())
    return Result.Success(vouchers)
}

sealed class Result<out E, out V> {
    data class Success<out V>(val value: V) : Result<Nothing, V>()
    data class Error<out E>(val value: E) : Result<E, Nothing>()
}

sealed class ValidationError : Exception() {
    class VouchersStringIsBlankOrEmpty : ValidationError()
}