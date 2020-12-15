//
//  Day15.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-15.
//

import Foundation

struct Day15: Day {

    static func first() -> String {
        play(until: 2020).description
    }

    static func second() -> String {
        play(until: 30000000).description
    }

    private static func play(until count: Int) -> Int {
        let memory = UnsafeMutableBufferPointer<Int>.allocate(capacity: count * 2)
        memory.assign(repeating: -1)
        let ints = day15_input.lines(separator: ",").compactMap(Int.init)
        ints.enumerated().forEach { turn, value in
            memory[value * 2] = turn
        }

        var last = ints.last!
        (ints.count..<count).forEach { turn in
            let recent = memory[last * 2]
            let oneBeforeThat = memory[last * 2 + 1]
            if oneBeforeThat == -1 {
                last = 0
            } else {
                last = (recent - oneBeforeThat)
            }
            memory[last * 2 + 1] = memory[last * 2]
            memory[last * 2] = turn
        }
        return last
    }
}
