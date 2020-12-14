//
//  Day14.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-14.
//

import Foundation

struct Day14: Day {

    struct Memory {
        var currentBitmask: Bitmask = .init()
        var values: [Int: Int36] = [:]

        mutating func writeFirst(_ write: Write) {
            values[write.offset.backing] = currentBitmask.maskValue(write.value)
        }

        mutating func writeSecond(_ write: Write) {
            currentBitmask.inflateAddesses(from: write.offset).forEach { offset in
                values[offset] = write.value
            }
        }
    }

    struct Bitmask {
        var bits: [Bit?] = .init(repeating: nil, count: 36)

        func maskValue(_ int: Int36) -> Int36 {
            var out = int
            bits.reversed().enumerated().forEach { offset, bit in
                if let bit = bit {
                    out[offset] = bit
                }
            }
            return out
        }

        func inflateAddesses(from offset: Int36) -> [Int] {
            var floating: [Int] = []
            // first write all the ones
            var memory = offset
            bits.reversed().enumerated().forEach { offset, bit in
                if bit == .one { memory[offset] = .one }
                if bit == nil { floating.append(offset) }
            }
            // then create permutations from the nil ones
            var out: [Int36] = []
            inflateRecursively(into: &out, from: memory, at: floating[...])
            return out.map(\.backing)
        }

        private func inflateRecursively(into result: inout [Int36], from int: Int36, at indices: ArraySlice<Int>) {
            if indices.count == 0 { return }
            var int = int
            let index = indices.first!
            int[index] = .zero
            result.append(int)
            inflateRecursively(into: &result, from: int, at: indices.dropFirst())
            int[index] = .one
            result.append(int)
            inflateRecursively(into: &result, from: int, at: indices.dropFirst())
        }
    }

    struct Write {
        var offset: Int36
        var value: Int36
    }

    struct Int36 {
        var backing: Int

        subscript(offset: Int) -> Bit {
            get {
                return (backing & (1 << offset)) == 0 ? .zero : .one
            }
            set {
                switch newValue {
                case .zero: backing &= ~(1 << offset)
                case .one: backing |= (1 << offset)
                }
            }
        }
    }

    enum Bit {
        case zero, one
    }

    static func first() -> String {
        var memory = Memory()
        day14_input.lines().forEach { line in
            if line.hasPrefix("mask = ") {
                memory.currentBitmask = Bitmask(string: line)
            }
            if line.hasPrefix("mem") {
                memory.writeFirst(Write(string: line))
            }
        }

        let total = memory.values.values.reduce(0, { $0 + $1.backing })
        return total.description
    }

    static func second() -> String {
        var memory = Memory()
        day14_input.lines().forEach { line in
            if line.hasPrefix("mask = ") {
                memory.currentBitmask = Bitmask(string: line)
            }
            if line.hasPrefix("mem") {
                memory.writeSecond(Write(string: line))
            }
        }

        let total = memory.values.values.reduce(0, { $0 + $1.backing })
        return total.description
    }
}

extension Day14.Bitmask: PatternConvertible {
    static let pattern: String = "mask = (.+)"
    init(match: PatternMatch) {
        self.init(bits: match.string(at: 0).map { char -> Day14.Bit? in
            switch char {
            case "0": return .zero
            case "1": return .one
            default: return nil
            }
        })
    }
}

extension Day14.Write: PatternConvertible {
    static let pattern: String = "mem\\[(\\d+)\\] = (\\d+)"
    init(match: PatternMatch) {
        self.init(offset: .init(backing: match.int(at: 0)), value: .init(backing: match.int(at: 1)))
    }
}

extension Day14.Bitmask: CustomStringConvertible {
    var description: String {
        String(bits.map { bit in
            switch bit {
            case .one: return "1"
            case .zero: return "0"
            default: return "X"
            }
        })
    }
}

extension Day14.Int36: CustomStringConvertible {
    var description: String {
        backing.description
    }
}
