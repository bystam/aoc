//
//  Day2.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-06.
//

import Foundation

struct Day2: Day {
    struct Line: PatternConvertible {
        static let pattern: String = "(.*)-(.*) (.*): (.*)"

        let min: Int
        let max: Int
        let char: Character
        let password: String

        init(match: PatternMatch) {
            min = match.int(at: 0)
            max = match.int(at: 1)
            char = match.character(at: 2)
            password = match.string(at: 3)
        }

        var isValidFirst: Bool {
            let occurances = password.countWhere { $0 == char }
            return occurances >= min && occurances <= max
        }

        var isValidSecond: Bool {
            let firstMatch = password.char(at: min - 1) == char
            let secondMatch = password.char(at: max - 1) == char
            return firstMatch != secondMatch
        }
    }

    static func first() -> String {
        day2_input.lines(Line.self)
            .countWhere(\.isValidFirst)
            .description
    }

    static func second() -> String {
        day2_input.lines(Line.self)
            .countWhere(\.isValidSecond)
            .description
    }
}
