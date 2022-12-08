
fun Iterable<Int>.product(): Int {
    return fold(1) { acc, next -> acc * next }
}
