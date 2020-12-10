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

        permutations.indices.forEach { index in
            let jolt = adapters[index]
            var total = 0
            if (index - 3) < 0 && jolt <= 3 {
                total += 1 // there's an extra way to jump into here
            }

            let lookBack = max(index - 3, 0)
            (lookBack..<index).forEach { behind in
                if (jolt - adapters[behind]) <= 3 { // can reach it
                    total += permutations[behind]
                }
            }

            permutations[index] = total
        }

        return permutations.last!.description
    }
}
