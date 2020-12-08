//
//  Utils.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-06.
//

import Foundation

func cross<A: Sequence, B: Sequence>(_ a: A, _ b: B) -> AnySequence<(A.Element, B.Element)> {
    let seq = a.lazy.flatMap { aVal in
        b.lazy.map { bVal in
            (aVal, bVal)
        }
    }
    return AnySequence(seq)
}

func cross<A: Sequence, B: Sequence, C: Sequence>(
    _ a: A, _ b: B, _ c: C
) -> AnySequence<(A.Element, B.Element, C.Element)> {
    let seq = a.lazy.flatMap { aVal in
        b.lazy.flatMap { bVal in
            c.lazy.map { cVal in
                (aVal, bVal, cVal)
            }
        }
    }
    return AnySequence(seq)
}

extension Sequence {
    func countWhere(_ predicate: (Element) -> Bool) -> Int {
        return lazy.filter(predicate).count
    }
}

extension String {
    func char(at index: Int) -> Character {
        let i = self.index(startIndex, offsetBy: index)
        return self[i]
    }
}

extension NSRegularExpression {
    private static var cache: [String: NSRegularExpression] = [:]

    static func compile(_ pattern: String) -> NSRegularExpression {
        if let cached = cache[pattern] {
            return cached
        }
        let exp = try! NSRegularExpression(pattern: pattern, options: [])
        cache[pattern] = exp
        return exp
    }
}
