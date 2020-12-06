//
//  Utils.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-06.
//

import Foundation

func cross<A: Sequence, B: Sequence>(_ a: A, _ b: B) -> AnySequence<(A.Element, B.Element)> {
    let seq = a.lazy.flatMap { aVal -> [(A.Element, B.Element)] in
        return b.lazy.map { bVal -> (A.Element, B.Element) in
            (aVal, bVal)
        }
    }
    return AnySequence(seq)
}
