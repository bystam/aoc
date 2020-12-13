//
//  Day13.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-13.
//

import Foundation

struct Day13: Day {

    static func first() -> String {
        let lines = day13_input.lines()
        let arrival = Int(lines[0])!
        let buses = lines[1].components(separatedBy: ",").compactMap(Int.init)

        let alternatives: [((bus: Int, delay: Int))] = buses
            .map { bus in (bus, bus - (arrival % bus)) }
        let (bus, delay) = alternatives.min(by: \.delay)!

        return (bus * delay).description
    }

    static func second() -> String {
        let lines = day13_input.lines()
        let entries = lines[1].components(separatedBy: ",")
        let equations = entries.enumerated().compactMap { offset, entry -> String? in
            if let bus = Int(entry) {
                return "y + \(offset) = \(bus) * x\(offset)"
            }
            return nil
        }
        // run this system in wolfram alpha, `y` is the answer
        return equations.joined(separator: ",")
    }
}
