//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

final class Day5: Solver {

    private struct Unit {
        let raw: UnicodeScalar

        init(raw: UnicodeScalar) {
            self.raw = raw
        }

        init(character: Character) {
            self.init(raw: character.unicodeScalars.first!)
        }

        func reacts(with other: Unit) -> Bool {
            return raw.value ^ other.raw.value == 0b100000
        }

        func isSameType(as other: Unit) -> Bool {
            return (raw.value & 0b011111) == (other.raw.value & 0b011111)
        }
    }

    func solveFirst(input: Input) throws -> String {
        let polymer = input.string().map(Unit.init)
        return optimizedPolymerLength(polymer).description
    }

    func solveSecond(input: Input) throws -> String {
        let polymer = input.string().map(Unit.init)
        let types = "abcdefghijklmnopqrstuvwxyz".map(Unit.init)
        return types
            .map { type in strippedPolymer(polymer, matching: type) }
            .reduce(into: Int.max, { acc, stripped in
                acc = min(acc, optimizedPolymerLength(stripped))
            })
            .description
    }

    private func optimizedPolymerLength(_ units: [Unit]) -> Int {
        var optimized = units
        var index = 0
        while index < (optimized.count - 1) {
            let currentPolymer = optimized[index]
            let nextPolymer = optimized[index+1]
            if currentPolymer.reacts(with: nextPolymer) {
                optimized.removeSubrange(index...index+1)
                index = max(index-1, 0)
            } else {
                index += 1
            }
        }
        return optimized.count
    }

    private func strippedPolymer(_ units: [Unit], matching type: Unit) -> [Unit] {
        return units.filter { !$0.isSameType(as: type) }
    }
}
