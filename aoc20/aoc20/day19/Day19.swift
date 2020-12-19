//
//  Day19.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-19.
//

import Foundation

struct Day19: Day {

    typealias RuleID = Int

    enum Rule {
        case exact(Character)
        case predicate(Predicate)
    }

    struct Predicate {
        var disjunctions: [[RuleID]]
    }

    struct Rules {
        var ruleById: [RuleID : Rule]

        func isValid(_ message: String, for id: RuleID) -> Bool {
            return match(message[...], with: ruleById[id]!).contains("")
        }

        private func match(_ message: Substring, with rule: Rule) -> [Substring] {
            switch rule {
            case .exact(let char):
                return message.first == char ? [message.dropFirst()] : []
            case .predicate(let predicate):
                return predicate.disjunctions.flatMap { ids -> [Substring] in
                    ids.reduce([message]) { acc, id -> [Substring] in
                        acc.flatMap { match($0, with: ruleById[id]!) }
                    }
                }
            }
        }
    }

    static func first() -> String {
        let parts = day19_input.lines(separator: "\n\n")
        let rules = parseRules(parts[0].components(separatedBy: "\n"))
        let messages = parts[1].components(separatedBy: "\n")
        let valids = messages.filter { message -> Bool in
            rules.isValid(message, for: 0)
        }
        return valids.count.description
    }

    static func second() -> String {
        let parts = day19_input.lines(separator: "\n\n")
        let ruleStrings = parts[0]
            .replacingOccurrences(of: "8: 42", with: "8: 42 | 42 8")
            .replacingOccurrences(of: "11: 42 31", with: "11: 42 31 | 42 11 31")
        let rules = parseRules(ruleStrings.components(separatedBy: "\n"))
        let messages = parts[1].components(separatedBy: "\n")
        let valids: [String] = messages.filter { message -> Bool in
            rules.isValid(message, for: 0)
        }
        return valids.count.description
    }

    private static func parseRules(_ lines: [String]) -> Rules {
        var result: [RuleID: Rule] = [:]
        for line in lines {
            let outer = line.components(separatedBy: ": ")
            let ruleId = RuleID(outer[0])!

            if outer[1].hasPrefix("\"") {
                let char = outer[1].replacingOccurrences(of: "\"", with: "")
                result[ruleId] = .exact(char.first!)
                continue
            }

            let disStrings = outer[1].components(separatedBy: " | ")
            let disjunctions = disStrings.map { dis -> [RuleID] in
                dis.components(separatedBy: " ").compactMap(RuleID.init)
            }

            result[ruleId] = .predicate(.init(disjunctions: disjunctions))
        }
        return Rules(ruleById: result)
    }
}
