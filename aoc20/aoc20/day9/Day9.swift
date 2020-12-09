//
//  Day9.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-09.
//

import Foundation

struct Day9: Day {

    static func first() -> String {
        let ints = day9_input.lines().compactMap(Int.init)
        return findInvalidNumber(in: ints).description
    }

    static func second() -> String {
        let ints = day9_input.lines().compactMap(Int.init)
        let number = findInvalidNumber(in: ints)
        var target: ArraySlice<Int>!
        outerloop: for start in ints.indices {
            var sum = 0
            for end in ints.indices.dropFirst(start) {
                sum += ints[end]
                if sum == number {
                    target = ints[start...end]
                    break outerloop
                }
            }
        }

        return (target.min()! + target.max()!).description
    }

    private static func findInvalidNumber(in ints: [Int]) -> Int {
        func isSumOfTwo(_ value: Int, in numbers: ArraySlice<Int>) -> Bool {
            cross(numbers, numbers).contains { a, b -> Bool in
                a != b && (a + b) == value
            }
        }

        let index = ints.indices.dropFirst(25).first(where: { index in
            let value = ints[index]
            let numbers = ints[index-25..<index]
            return isSumOfTwo(value, in: numbers) == false
        })!
        return ints[index]
    }
}
