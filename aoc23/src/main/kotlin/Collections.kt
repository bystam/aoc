
fun Iterable<Int>.product(): Int {
    return fold(1) { acc, next -> acc * next }
}

fun String.splitIgnoreEmpty(delimiter: String) = this.split(delimiter).filter { it.isNotEmpty() }

operator fun <E> List<E>.component6(): E {
    return get(5)
}
