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

        let alternatives: [(bus: Int, delay: Int)] = buses
            .map { bus in (bus, bus - (arrival % bus)) }
        let (bus, delay) = alternatives.min(by: \.delay)!

        return (bus * delay).description
    }

    static func second() -> String {
        let lines = day13_input.lines()
        let entries = lines[1].components(separatedBy: ",")

        var result = 0
        var step = 1
        entries.enumerated().forEach { offset, entry in
            guard let bus = Int(entry) else { return }
            while (result + offset) % bus != 0 {
                result += step
            }
            step *= bus
        }
        return result.description
    }
}
