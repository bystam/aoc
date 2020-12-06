//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

struct RingBuffer<Element> {

    struct Index: Equatable, Comparable {
        fileprivate let value: Int

        static func <(lhs: Index, rhs: Index) -> Bool {
            return lhs.value < rhs.value
        }
    }

    private var buffer: [Element?]
    private var head: Index

    init(count: Int) {
        buffer = [Element?](repeating: nil, count: count)
        head = Index(value: 0)
    }

    mutating func append(_ element: Element) {
        buffer[head.value % buffer.count] = element
        head = Index(value: head.value + 1)
    }

    mutating func append(_ elements: [Element]) {
        elements.forEach { self.append($0) }
    }
}

extension RingBuffer: Collection {

    var startIndex: Index {
        let value = head.value - buffer.count
        return Index(value: Swift.max(value, 0))
    }

    var endIndex: Index {
        return Index(value: head.value)
    }

    subscript(index: Index) -> Element {
        let count = Swift.min(buffer.count, head.value)
        return buffer[index.value % count]!
    }

    func index(after index: Index) -> Index {
        return Index(value: index.value + 1)
    }
}
