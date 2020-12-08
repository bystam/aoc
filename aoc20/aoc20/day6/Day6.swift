//
//  Day6.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-08.
//

import Foundation

struct Day6: Day {

    struct Group {
        var answersPerPerson: [Set<Character>]

        var allAnswers: Set<Character> {
            answersPerPerson.reduce(into: Set()) { acc, set in
                acc.formUnion(set)
            }
        }

        var commonAnswers: Set<Character> {
            answersPerPerson.reduce(into: answersPerPerson[0]) { acc, set in
                acc.formIntersection(set)
            }
        }

        init(string: String) {
            self.answersPerPerson = string.components(separatedBy: .whitespacesAndNewlines)
                .map { Set($0) }
        }
    }

    static func first() -> String {
        let groups = day6_input.lines(separator: "\n\n").map(Group.init)
        return groups.map { $0.allAnswers.count }.reduce(0, +).description
    }

    static func second() -> String {
        let groups = day6_input.lines(separator: "\n\n").map(Group.init)
        return groups.map { $0.commonAnswers.count }.reduce(0, +).description
    }
}
