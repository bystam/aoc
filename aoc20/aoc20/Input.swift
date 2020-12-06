//
//  Input.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-06.
//

import Foundation

struct Input {
    private let string: String
}

extension Input {
    func lines() -> [String] {
        string.components(separatedBy: "\n")
    }

    func lines<O>(_ mapper: (String) -> O?) -> [O] {
        lines().compactMap(mapper)
    }
}

extension Input: ExpressibleByStringLiteral {
    init(stringLiteral value: StaticString) {
        self.string = value.description
    }
}
