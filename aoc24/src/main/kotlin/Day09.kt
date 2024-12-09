
object Day09: Day {

    @JvmStatic
    fun main(args: Array<String>) = solve(Day09)

    override fun task1(input: Input): Any {
        val blocks = readBlocks(input)

        var head = 0
        var tail = blocks.lastIndex
        while (head < tail) {
            while (blocks[head] != null) {
                head += 1
            }
            while (blocks[tail] == null) {
                tail -= 1
            }
            blocks[head] = blocks[tail]
            blocks[tail] = null
        }

        var checksum = 0L
        blocks.filterNotNull().forEachIndexed { index, fileId ->
            checksum += (index * fileId)
        }

        return checksum
    }

    override fun task2(input: Input): Any {
        val blocks = readBlocks(input)

        val fileIds = blocks.filterNotNull().toSet().sortedDescending()
        for (fileId in fileIds) {
            val fileStart = blocks.indexOf(fileId)
            val fileEnd = blocks.lastIndexOf(fileId)

            val fileSize = fileEnd - fileStart + 1
            val freeSpace = (0 until fileSize).map { null }
            val possibleSpace = blocks.indexOfSublist(freeSpace)

            if (possibleSpace != -1 && possibleSpace < fileStart) {
                repeat(fileSize) {
                    blocks[possibleSpace + it] = fileId
                    blocks[fileStart + it] = null
                }
            }
        }

        var checksum = 0L
        blocks.forEachIndexed { index, fileId ->
            if (fileId != null) {
                checksum += (index * fileId)
            }
        }

        return checksum
    }

    private fun readBlocks(input: Input): MutableList<Int?> {
        val blockDescriptions = input.text().map { it.digitToInt() }
        val blocks = mutableListOf<Int?>()

        var isFile = true
        var fileId = 0
        for (blockDescription in blockDescriptions) {
            repeat(blockDescription) {
                if (isFile) {
                    blocks.add(fileId)
                } else {
                    blocks.add(null)
                }
            }
            if (isFile) {
                fileId += 1
            }
            isFile = !isFile
        }
        return blocks
    }

    private fun <T> List<T>.indexOfSublist(sublist: List<T>): Int {
        var matching = 0
        for (index in indices) {
            if (matching == sublist.size) {
                return index - matching
            }
            if (this[index] == sublist[matching]) {
                matching += 1
            } else {
                matching = 0
            }
        }
        return -1
    }
}
