//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

final class Day8: Solver {

    fileprivate struct Node {
        var children: [Node]
        let metadata: [Int]
    }

    func solveFirst(input: Input) throws -> String {
        return readTree(from: input.string())
            .dfs()
            .reduce(into: 0, { acc, node in
                acc += node.metadata.reduce(0, +)
            })
            .description
    }

    func solveSecond(input: Input) throws -> String {
        return readTree(from: input.string())
            .value()
            .description
    }

    private func readTree(from string: String) -> Node {
        let scanner = Scanner(string: string)
        return readNode(in: scanner)
    }

    private func readNode(in scanner: Scanner) -> Node {
        var childCount: Int = 0
        var metadataCount: Int = 0
        scanner.scanInt(&childCount)
        scanner.scanInt(&metadataCount)
        return Node(children: readChildren(count: childCount, in: scanner),
                    metadata: readMetadata(count: metadataCount, in: scanner))
    }

    private func readChildren(count: Int, in scanner: Scanner) -> [Node] {
        return (0..<count).map { _ in
            return readNode(in: scanner)
        }
    }

    private func readMetadata(count: Int, in scanner: Scanner) -> [Int] {
        return (0..<count).reduce(into: [Int](), { acc, _ in
            var metadata: Int = 0
            scanner.scanInt(&metadata)
            acc.append(metadata)
        })
    }
}

private extension Day8.Node {

    struct DFS: Sequence {

        let root: Day8.Node

        func makeIterator() -> AnyIterator<Day8.Node> {
            var next = 0
            var order: [Day8.Node] = []
            dfs(node: root, into: &order)
            return AnyIterator<Day8.Node> { () -> Day8.Node? in
                let index = next
                next += 1
                return order.indices.contains(index) ? order[index] : nil
            }
        }

        private func dfs(node: Day8.Node, into order: inout [Day8.Node]) {
            order.append(node)
            node.children.forEach { child in
                dfs(node: child, into: &order)
            }
        }
    }

    func dfs() -> DFS {
        return DFS(root: self)
    }
}

private extension Day8.Node {

    func value() -> Int {
        if children.isEmpty {
            return metadata.reduce(0, +)
        }

        return metadata
            .filter { $0 > 0 }
            .map { $0 - 1 }
            .reduce(into: 0, { acc, index in
                if children.indices.contains(index) {
                    acc += children[index].value()
                }
        })
    }

}
