//
//  Day7.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-08.
//

import Foundation

struct Day7: Day {

    struct Rule: PatternConvertible {
        static let pattern: String = "(.*) bags contain (.*)\\."

        var source: Bag
        var content: [BagQuantity]

        init(match: PatternMatch) {
            self.source = Bag(string: match.string(at: 0))
            self.content = match.string(at: 1).components(separatedBy: ", ")
                .compactMap { string in
                    if string.hasSuffix("no other bags") { return nil }
                    return BagQuantity(string: string)
                }
        }
    }

    struct BagQuantity: PatternConvertible, Hashable {
        static let pattern: String = "(\\d+) (.*) bag(?:[s]?)"

        var count: Int
        var bag: Bag

        init(count: Int, bag: Bag) {
            self.count = count
            self.bag = bag
        }

        init(match: PatternMatch) {
            self.count = match.int(at: 0)
            self.bag = Bag(string: match.string(at: 1))
        }
    }

    struct Bag: StringType {
        static let root: Bag = "shiny gold"

        var string: String
    }

    static func first() -> String {
        let rules = day7_input.lines(Rule.self)
        let graph: [Bag: [Bag]] = rules.reduce(into: [:]) { acc, rule in
            rule.content.forEach {
                acc[$0.bag, default: []].append(rule.source)
            }
        }

        var count = 0
        var visited = Set<Bag>()
        var queue: ArraySlice<Bag> = [.root]
        while !queue.isEmpty {
            let next = queue.removeFirst()
            if !visited.contains(next) {
                visited.insert(next)
                queue += graph[next] ?? []
                count += 1
            }
        }
        // -1 because it also counts the root itself
        return (count - 1).description
    }

    static func second() -> String {
        let rules = day7_input.lines(Rule.self)
        let graph: [Bag: [BagQuantity]] = rules.reduce(into: [:]) { acc, rule in
            acc[rule.source] = rule.content
        }

        var count = 0
        var queue: ArraySlice<BagQuantity> = [BagQuantity(count: 1, bag: .root)]
        while !queue.isEmpty {
            let next = queue.removeFirst()
            (0..<next.count).forEach { _ in
                count += 1
                queue += graph[next.bag] ?? []
            }
        }
        // -1 because it also counts the root itself
        return (count - 1).description
    }
}
