//
//  Day1.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-06.
//

import Foundation

struct Day1: Day {
    static func first() -> String {
        let ints = day1_input.lines(Int.init)
        let tuple = cross(ints, ints).first(where: { $0 + $1 == 2020 })!
        return (tuple.0 * tuple.1).description
    }

    static func second() -> String {
        let ints = day1_input.lines(Int.init)
        let tuple = cross(ints, ints, ints).first(where: { $0 + $1 + $2 == 2020 })!
        return (tuple.0 * tuple.1 * tuple.2).description
    }
}
