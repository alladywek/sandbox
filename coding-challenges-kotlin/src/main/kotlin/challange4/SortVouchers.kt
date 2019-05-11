package challange4

fun sortVouchers(vouchers: String): Result<ValidationError, String> {
    return Result.Success(vouchers)
}

sealed class Result<out E, out V> {
    data class Success<out V>(val value: V): Result<Nothing, V>()
    data class Error<out E>(val value: E): Result<E, Nothing>()
}

sealed class ValidationError(msg: String): Exception(msg)