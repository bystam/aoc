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
    func lines(separator: String = "\n") -> [String] {
        string.components(separatedBy: separator)
    }

    func lines<O>(_ mapper: (String) -> O?) -> [O] {
        lines().compactMap(mapper)
    }

    func lines<O: PatternConvertible>(_ type: O.Type) -> [O] {
        let regex = NSRegularExpression.compile(type.pattern)
        return lines().map { line in
            let range = NSRange(location: 0, length: line.utf16.count)
            let backing = regex.matches(in: line, options: [], range: range)[0]
            let match = PatternMatch(string: line, backing: backing)
            return O(match: match)
        }
    }
}

extension Input: ExpressibleByStringLiteral {
    init(stringLiteral value: StaticString) {
        self.string = value.description
    }
}

// MARK: - Regexp parsing

protocol PatternConvertible {
    static var pattern: String { get }

    init(match: PatternMatch)
}

struct PatternMatch {
    fileprivate let string: String
    fileprivate let backing: NSTextCheckingResult

    func int(at index: Int) -> Int {
        let range = backing.range(at: index + 1)
        return Int(string[Range(range, in: string)!])!
    }

    func character(at index: Int) -> Character {
        let range = backing.range(at: index + 1)
        return string[Range(range, in: string)!].first!
    }

    func string(at index: Int) -> String {
        let range = backing.range(at: index + 1)
        return String(string[Range(range, in: string)!])
    }

    func parse<T: RawRepresentable>(_ type: T.Type, at index: Int) -> T where T.RawValue: LosslessStringConvertible {
        let s = string(at: index)
        let raw = T.RawValue(s)!
        return T(rawValue: raw)!
    }
}

extension PatternConvertible {
    init(string: String) {
        let regex = NSRegularExpression.compile(Self.pattern)
        let range = NSRange(location: 0, length: string.utf16.count)
        let backing = regex.matches(in: string, options: [], range: range)[0]
        let match = PatternMatch(string: string, backing: backing)
        self.init(match: match)
    }
}

extension NSRegularExpression {
    private static var cache: [String: NSRegularExpression] = [:]

    fileprivate static func compile(_ pattern: String) -> NSRegularExpression {
        if let cached = cache[pattern] {
            return cached
        }
        let exp = try! NSRegularExpression(pattern: pattern, options: [])
        cache[pattern] = exp
        return exp
    }
}
