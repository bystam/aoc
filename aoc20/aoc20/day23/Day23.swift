//
//  Day23.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-23.
//

import Foundation

struct Day23: Day {

    final class Cup {

        static let minLabel: Int = 1

        let label: Int
        unowned(unsafe) var next: Cup?

        init(label: Int) {
            self.label = label
        }

        func cutAway3() -> (head: Cup, tail: Cup) {
            let head = next!
            let tail = next!.next!.next!
            next = tail.next
            return (head, tail)
        }

        func insert(head: Cup, tail: Cup) {
            tail.next = next
            next = head
        }
    }

    static func first() -> String {
        let cups = createCups(start: [9, 5, 2, 3, 1, 6, 4, 8, 7])
        play(rounds: 100, with: cups)
        let answer = cups
            .first(where: { $0.label == 1 })!
            .dropFirst()
            .map(\.label.description).joined()
        return answer
    }

    static func second() -> String {
        let start = [9, 5, 2, 3, 1, 6, 4, 8, 7]
        var cups = Array(repeating: 0, count: 1_000_000)
        for i in start.indices.dropLast() {
            cups[start[i] - 1] = start[i + 1]
        }
        for c in 10..<1_000_000 {
            cups[c - 1] = c + 1
        }
        cups[1_000_000 - 1] = 9
        cups[7 - 1] = 10

        var current = cups[0]
        for _ in (0..<10_000_000) {
            var (a, b, c) = (0, 0, 0)
            a = cups[current - 1]
            b = cups[a - 1]
            c = cups[b - 1]
            cups[current - 1] = cups[c - 1]

            var destination = current
            repeat {
                destination -= 1
                if destination < 1 { destination = 1_000_000 }
            } while (a == destination || b == destination || c == destination)

            let next = cups[destination - 1]
            cups[destination - 1] = a
            cups[c - 1] = next
            current = cups[current - 1]
        }

        let answer = cups[0] * cups[cups[0] - 1]
        return answer.description
    }

    private static func play(rounds: Int, with cups: [Cup]) {
        let lookup = cups.sorted(by: { $0.label < $1.label })
        var current = cups[0]
        for _ in (0..<rounds) {
            let pickedUp = current.cutAway3()

            var destinationLabel = current.label
            repeat {
                destinationLabel -= 1
                if destinationLabel < 1 { destinationLabel = cups.count }
            } while (pickedUp.head.label == destinationLabel ||
                        pickedUp.head.next!.label == destinationLabel ||
                        pickedUp.head.next!.next!.label == destinationLabel)

            let destination = lookup[destinationLabel - 1]
            destination.insert(head: pickedUp.head, tail: pickedUp.tail)
            current = current.next!
        }
    }

    private static func createCups(start: [Int], padding: ClosedRange<Int>? = nil) -> [Cup] {
        var cups = start.map(Cup.init(label:))
        if let padding = padding {
            cups += padding.map(Cup.init(label:))
        }
        (0..<cups.count).forEach { i in
            cups[i].next = cups[(i + 1) % cups.count]
        }
        return cups
    }
}

extension Day23.Cup: Sequence {

    func makeIterator() -> AnyIterator<Day23.Cup> {
        var next: Day23.Cup? = self
        return AnyIterator {
            let ret = next
            next = next?.next
            if next?.label == self.label {
                next = nil
            }
            return ret
        }
    }
}
