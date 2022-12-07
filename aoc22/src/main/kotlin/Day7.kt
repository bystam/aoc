import java.lang.StringBuilder
import java.nio.file.Path
import java.util.LinkedList

class Day7 : Day {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = solve(Day7())
    }

    override val testInput: String? = """
        ${'$'} cd /
        ${'$'} ls
        dir a
        14848514 b.txt
        8504156 c.dat
        dir d
        ${'$'} cd a
        ${'$'} ls
        dir e
        29116 f
        2557 g
        62596 h.lst
        ${'$'} cd e
        ${'$'} ls
        584 i
        ${'$'} cd ..
        ${'$'} cd ..
        ${'$'} cd d
        ${'$'} ls
        4060174 j
        8033020 d.log
        5626152 d.ext
        7214296 k
    """.trimIndent()

    override fun task1(input: Input): Any {
        val root = parse(input)
        return root.dfs()
            .filterIsInstance<Node.Dir>()
            .filter { it.size < 100000 }
            .sumOf { it.size }
    }

    override fun task2(input: Input): Any {
        val root = parse(input)
        val unusedSpace = 70000000L - root.size
        val required = 30000000L - unusedSpace
        val dirs = root.dfs()
            .filterIsInstance<Node.Dir>()
            .sortedBy { it.size }
        val dir = dirs.first { it.size > required }
        return dir.size
    }

    private fun parse(input: Input): Node.Dir {
        var currentDir: Node.Dir = Node.Dir(Path.of("/"))
        val lookup = mutableMapOf(
            currentDir.path to currentDir
        )
        var listing = false
        for (line in input.lines) {
            when {
                line.startsWith("$ cd") -> {
                    listing = false
                    val subpath = line.removePrefix("$ cd ")

                    val newPath = if (subpath == "..")
                        currentDir.path.parent
                    else
                        currentDir.path.resolve(subpath)
                    currentDir = lookup.getOrPut(newPath) { Node.Dir(newPath) }
                }

                line == "$ ls" -> {
                    listing = true
                }

                else -> {
                    assert(listing)
                    val (prefix, name) = line.split(" ")
                    val childPath = currentDir.path.resolve(name)
                    val child = if (prefix == "dir") {
                        Node.Dir(childPath).also {
                            lookup[childPath] = it
                        }
                    } else {
                        Node.File(childPath, prefix.toLong())
                    }
                    currentDir.children.add(child)
                }
            }
        }

        return lookup[Path.of("/")]!!
    }

    sealed class Node(val path: Path) {
        abstract val size: Long
        class File(
            path: Path,
            override val size: Long
        ) : Node(path)

        class Dir(
            path: Path
        ) : Node(path) {
            val children: MutableList<Node> = mutableListOf()

            override val size: Long get() = children.sumOf { it.size }
        }

        fun dfs(): List<Node> {
            val result = mutableListOf<Node>()
            val queue = LinkedList<Node>().also { it.add(this) }
            while (queue.isNotEmpty()) {
                val next = queue.removeFirst()
                result.add(next)
                if (next is Dir) {
                    queue.addAll(next.children)
                }
            }
            return result
        }

        override fun toString(): String {
            val builder = StringBuilder()
            stringify("- ", builder)
            return builder.toString()
        }

        private fun stringify(prefix: String, builder: StringBuilder) {
            when (this) {
                is Dir -> {
                    builder.append(prefix).append(path.fileName ?: path).append(" (dir)").append('\n')
                    children.forEach {
                        it.stringify("  $prefix", builder)
                    }
                }

                is File -> {
                    builder.append(prefix).append(path.fileName).append(" (file, size=$size)").append('\n')
                }
            }
        }
    }
}
