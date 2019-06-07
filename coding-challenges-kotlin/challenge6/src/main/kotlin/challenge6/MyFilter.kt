package challenge6

inline fun <reified T> myFilter(src: Array<T>, noinline filter: (T) -> Boolean): Array<T> {
    if (src.isEmpty()) return emptyArray()
    return myFilter(src, emptyArray(), filter)
}

tailrec fun <T> myFilter(src: Array<T>, dest: Array<T>, filter: (T) -> Boolean): Array<T> {
    return if (src.isEmpty())
        dest
    else myFilter(src.tail, if (filter(src.head)) { dest + src.head } else dest, filter)
}

private val <T> Array<T>.tail: Array<T> get() = if (size == 1) copyOf(0) as Array<T> else copyOfRange(1, size)
private val <T> Array<T>.head: T get() = get(0)