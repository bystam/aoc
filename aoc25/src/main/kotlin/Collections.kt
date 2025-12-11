
fun Iterable<Long>.product(): Long {
    return fold(1L) { acc, next -> acc * next }
}

fun String.splitIgnoreEmpty(delimiter: String) = this.split(delimiter).filter { it.isNotEmpty() }

operator fun <E> List<E>.component6(): E {
    return get(5)
}

fun <T> Collection<T>.powerset(): Sequence<Set<T>> =
    when (size) {
        0 -> sequenceOf(emptySet())
        else -> {
            val head = first()
            val tail = this - head
            tail.powerset() + tail.powerset().map { setOf(head) + it }
        }
    }
