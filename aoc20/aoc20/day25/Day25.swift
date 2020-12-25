//
//  Day25.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-25.
//

import Foundation

struct Day25: Day {

    private static let publicKeys: (Int, Int) = (9093927, 11001876)

    static func first() -> String {
        let (cardKey, doorKey) = publicKeys
        let cardLoop = findLoopSize(targeting: cardKey)
        let encryptionKey = (1...cardLoop).reduce(into: 1) { acc, _ in
            transform(&acc, subject: doorKey)
        }
        return encryptionKey.description
    }

    private static func findLoopSize(targeting target: Int) -> Int {
        var value = 1
        for loop in (1...) {
            transform(&value, subject: 7)
            if value == target {
                return loop
            }
        }
        fatalError()
    }

    private static func transform(_ value: inout Int, subject: Int) {
        value *= subject
        value %= 20201227
    }
}
