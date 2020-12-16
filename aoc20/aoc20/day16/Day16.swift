//
//  Day16.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-16.
//

import Foundation

struct Day16: Day {

    struct Rule {
        var field: String
        var range1: ClosedRange<Int>
        var range2: ClosedRange<Int>

        func matches(value: Int) -> Bool {
            range1.contains(value) || range2.contains(value)
        }
    }

    struct Ticket {
        var values: [Int]

        func isValid(by rules: [Rule]) -> Bool {
            values.allSatisfy { value in
                rules.contains(where: { $0.matches(value: value) })
            }
        }
    }

    static func first() -> String {
        let parts = day16_input.lines(separator: "\n\n")
        let rules = parts[0].components(separatedBy: "\n").map(Rule.init(string:))
        let nearbyTickets = parts[2].components(separatedBy: "\n")
            .dropFirst() // "nearby tickets:"
            .map(Ticket.init)

        let errorRate = nearbyTickets.lazy
            .flatMap { $0.values }
            .filter { value in
                rules.contains(where: { $0.matches(value: value) }) == false
            }
            .sum()

        return errorRate.description
    }

    static func second() -> String {
        let parts = day16_input.lines(separator: "\n\n")
        let rules = parts[0].components(separatedBy: "\n").map(Rule.init(string:))
        let myTicket = Ticket(string: parts[1].components(separatedBy: "\n")[1])
        let nearbyTickets = parts[2].components(separatedBy: "\n")
            .dropFirst() // "nearby tickets:"
            .map(Ticket.init)

        let validTickets = nearbyTickets
            .filter { ticket in
                ticket.isValid(by: rules)
            }

        var candidates: [Int: Set<String>] = [:]
        rules.forEach { rule in
            rules.indices.forEach { index in
                let isCandidate = validTickets.allSatisfy { ticket -> Bool in
                    rule.matches(value: ticket.values[index])
                }
                if isCandidate {
                    candidates[index, default: []].insert(rule.field)
                }
            }
        }

        let result = maxMatching(graph: candidates)

        var count = 1
        for (field, index) in result {
            if field.hasPrefix("departure") {
                count *= myTicket.values[index]
            }
        }
        return count.description
    }

    private static func maxMatching(
        graph: [Int: Set<String>]
    ) -> [String: Int] {
        var result: [String: Int] = [:]
        for index in graph.keys {
            var visited: Set<String> = []
            bipartiteMatch(graph: graph, index: index, visited: &visited, result: &result)
        }
        return result
    }

    @discardableResult
    private static func bipartiteMatch(
        graph: [Int: Set<String>],
        index: Int,
        visited: inout Set<String>,
        result: inout [String: Int]
    ) -> Bool {
        let fields = Set(graph.values.flatMap { $0 })
        for field in fields {
            guard graph[index]!.contains(field), !visited.contains(field) else { continue }
            visited.insert(field)

            let shouldReassign = result[field] == nil
                || bipartiteMatch(graph: graph, index: result[field]!, visited: &visited, result: &result)
            if shouldReassign {
                result[field] = index
                return true
            }
        }
        return false
    }
}

extension Day16.Rule: PatternConvertible {
    static let pattern: String = "(.*): (\\d+)-(\\d+) or (\\d+)-(\\d+)"

    init(match: PatternMatch) {
        self.init(
            field: match.string(at: 0),
            range1: (match.int(at: 1)...match.int(at: 2)),
            range2: (match.int(at: 3)...match.int(at: 4))
        )
    }
}

extension Day16.Ticket {
    init(string: String) {
        self.values = string.components(separatedBy: ",").compactMap(Int.init)
    }
}
