package challenge4

fun sortVouchers(vouchers: String): String {
    val (current, other) = vouchers
            .split(",")
            .map(String::buildVoucher)
            .partition { it.status.current }
    val sortedCurrent = current.sortedWith(compareBy({ it.endDate }, { it.status }, { it.id }))
    val sortedOther = other.sortedWith(
            compareByDescending<Voucher> { it.endDate }.thenComparing(compareBy({ it.status }, { it.id }))
    )
    return (sortedCurrent + sortedOther).joinToString(",")
}

private fun String.buildVoucher(): Voucher {
    return this.split(":").run { Voucher(get(0), VoucherStatus.valueOf(get(1)), get(2)) }
}

private class Voucher(val endDate: String, val status: VoucherStatus, val id: String) {
    override fun toString(): String = "$endDate:$status:$id"
}

private enum class VoucherStatus(val current: Boolean) {
    Activated(true),
    Available(true),
    Redeemed(false),
    Expired(false),
}