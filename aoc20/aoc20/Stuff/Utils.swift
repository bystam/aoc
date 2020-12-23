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

func cross<A: Sequence, B: Sequence, C: Sequence, D: Sequence>(
    _ a: A, _ b: B, _ c: C, _ d: D
) -> AnySequence<(A.Element, B.Element, C.Element, D.Element)> {
    let seq = a.lazy.flatMap { aVal in
        b.lazy.flatMap { bVal in
            c.lazy.flatMap { cVal in
                d.lazy.map { dVal in
                    (aVal, bVal, cVal, dVal)
                }
            }
        }
    }
    return AnySequence(seq)
}

extension Sequence {
    @inline(__always)
    func countWhere(_ predicate: (Element) -> Bool) -> Int {
        return lazy.filter(predicate).count
    }

    @inline(__always)
    func min<T: Comparable>(by transform: (Element) -> T) -> Element? {
        self.min(by: { transform($0) < transform($1) })
    }

    @inline(__always)
    func max<T: Comparable>(by transform: (Element) -> T) -> Element? {
        self.max(by: { transform($0) < transform($1) })
    }
}

extension String {
    func char(at index: Int) -> Character {
        let i = self.index(startIndex, offsetBy: index)
        return self[i]
    }
}

@inline(__always)
func measure(_ name: String, _ task: () -> Void) {
    let start = DispatchTime.now().rawValue
    task()
    let time =  DispatchTime.now().rawValue - start
    print("\(name) time: \(TimeInterval(time) / 1_000_000.0)")
}

@inline(__always)
func measure<T>(_ name: String, _ task: () -> T) -> T {
    let start = DispatchTime.now().rawValue
    let value = task()
    let time =  DispatchTime.now().rawValue - start
    print("\(name) time: \(TimeInterval(time) / 1_000_000.0)")
    return value
}
