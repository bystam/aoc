
fun Iterable<Int>.product(): Int {
    return fold(1) { acc, next -> acc * next }
}


operator fun <E> List<E>.component6(): E {
    return get(5)
}
