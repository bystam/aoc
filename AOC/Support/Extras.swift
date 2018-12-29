//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

prefix operator ^

prefix func ^<T, K>(_ keyPath: KeyPath<T, K>) -> (T) -> K {
    return { $0[keyPath: keyPath] }
}

func parseInt(_ string: String) -> Int {
    return Int(string)!
}

func product<T1: Sequence, T2: Sequence>(_ lhs: T1, _ rhs: T2) -> AnySequence<(T1.Iterator.Element, T2.Iterator.Element)> {
    return AnySequence (
        lhs.lazy.flatMap { x in rhs.lazy.map { y in (x,y) }}
    )
}

extension Sequence {

    func infinatelyRepeated() -> AnySequence<Element> {
        var iterator: Self.Iterator? = nil

        return AnySequence<Element> { () -> AnyIterator<Element> in
            return AnyIterator<Element> { () -> Element? in
                var element = iterator?.next()
                if element == nil {
                    iterator = self.makeIterator()
                    element = iterator?.next()
                }
                return element
            }
        }
    }

    func scan<Result>(_ initialResult: Result, _ nextPartialResult: @escaping (Result, Self.Element) -> Result) -> AnySequence<Result> {
        var acc: Result = initialResult
        var iterator = makeIterator()
        return AnySequence<Result> { () -> AnyIterator<Result> in
            return AnyIterator<Result> { () -> Result? in
                guard let next = iterator.next() else { return nil }
                acc = nextPartialResult(acc, next)
                return acc
            }
        }
    }

    func grouped<Identifier: Hashable>(by identity: (Element) throws -> Identifier) rethrows -> [Identifier: [Element]] {
        return try [Identifier: [Element]](grouping: self, by: identity)
    }

    func indexed<Identifier: Hashable>(by identity: (Element) throws -> Identifier) rethrows -> [Identifier: Element] {
        return try [Identifier: [Element]](grouping: self, by: identity)
            .mapValues { $0[0] }
    }

    func segmented(size: Int) -> AnySequence<[Element]> {
        var buffer: [Element] = Array(prefix(size))
        precondition(buffer.count == size)

        var isFirst = true
        var iterator = dropFirst(size).makeIterator()
        return AnySequence<[Element]> { () -> AnyIterator<[Element]> in
            return AnyIterator<[Element]> { () -> [Element]? in
                if isFirst {
                    isFirst = false
                    return buffer
                }
                guard let next = iterator.next() else { return nil }
                buffer.append(next)
                if buffer.count > size {
                    buffer.remove(at: 0)
                }
                if buffer.count == size {
                    return buffer
                }
                return nil
            }
        }
    }
}

extension Sequence where Element: Hashable {

    func countedOccurrences() -> [Element: Int] {
        return reduce(into: [:], { acc, element in
            return acc[element, default: 0] += 1
        })
    }
}

extension BidirectionalCollection where Element: Equatable {

    func lcs(with other: Self) -> [Element] {
        func lcsLength(_ other: Self) -> [[Int]] {
            var matrix = [[Int]](repeating: [Int](repeating: 0, count: other.count+1), count: self.count+1)

            for (i, selfElement) in self.enumerated() {
                for (j, otherElement) in other.enumerated() {
                    if otherElement == selfElement {
                        // Common char found, add 1 to highest lcs found so far.
                        matrix[i+1][j+1] = matrix[i][j] + 1
                    } else {
                        // Not a match, propagates highest lcs length found so far.
                        matrix[i+1][j+1] = Swift.max(matrix[i][j+1], matrix[i+1][j])
                    }
                }
            }
            return matrix
        }

        func backtrack(_ matrix: [[Int]]) -> [Element] {
            var i = self.count
            var j = other.count

            var currentIndex = self.endIndex

            var lcs: [Element] = []

            while i >= 1 && j >= 1 {
                // Indicates propagation without change: no new char was added to lcs.
                if matrix[i][j] == matrix[i][j - 1] {
                    j -= 1
                }
                    // Indicates propagation without change: no new char was added to lcs.
                else if matrix[i][j] == matrix[i - 1][j] {
                    i -= 1
                    currentIndex = self.index(before: currentIndex)
                }
                    // Value on the left and above are different than current cell.
                    // This means 1 was added to lcs length.
                else {
                    i -= 1
                    j -= 1
                    currentIndex = self.index(before: currentIndex)
                    lcs.append(self[currentIndex])
                }
            }

            lcs.reverse()
            return lcs
        }

        return backtrack(lcsLength(other))
    }
}
