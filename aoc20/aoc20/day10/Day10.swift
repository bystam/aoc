//
//  Day10.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-10.
//

import Foundation

struct Day10: Day {

    typealias Jolt = Int

    static func first() -> String {
        let adapters = day10_input.lines(Int.init).sorted()
        var ones = 0
        var threes = 1 // include the device itself
        if adapters[0] == 1 { ones += 1 }
        if adapters[0] == 3 { threes += 1 }

        adapters.indices.dropFirst(1).forEach { index in
            let diff = adapters[index] - adapters[index-1]
            if diff == 1 { ones += 1 }
            if diff == 3 { threes += 1 }
        }

        return (ones * threes).description
    }

    static func second() -> String {
        let adapters = day10_input.lines(Jolt.init).sorted()
        var permutations: [Int] = Array(repeating: 0, count: adapters.count)
        permutations[0] = 1
        if adapters[1] <= 3 {
            permutations[1] = permutations[0] + 1
        }
        if adapters[2] <= 3 {
            permutations[2] = permutations[0] + permutations[1] + 1
        }

        func consider(offset: Int, from current: Int, into result: inout Int) {
            let jolt = adapters[current]
            let behind = current + offset
            let joltBehind = adapters[safe: behind] ?? 0
            if (jolt - joltBehind) <= 3 {
                result += permutations[safe: behind] ?? 1
            }
        }

        permutations.drop(while: { $0 > 0 }).indices.forEach { index in
            var total = 0
            consider(offset: -3, from: index, into: &total)
            consider(offset: -2, from: index, into: &total)
            consider(offset: -1, from: index, into: &total)
            permutations[index] = total
        }

        return permutations.last!.description
    }
}
