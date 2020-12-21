//
//  StringType.swift
//
//  Created by Fredrik Bystam on 2020-07-23.
//

import Foundation

/// This is a way to enable usage of structs other than String
/// to represent string types, in order to not accidentally
/// mixing strings up. Tagging a struct with this gives a bunch of
/// automatic behaviors for that struct type.
protocol StringType: ExpressibleByStringLiteral, CustomStringConvertible, CustomDebugStringConvertible, Hashable, Comparable {
    var string: String { get }

    init(string: String)
}

extension StringType {
    static func <(lhs: Self, rhs: Self) -> Bool {
        lhs.string < rhs.string
    }
}

extension StringType {

    init(stringLiteral value: StaticString) {
        let string = value.withUTF8Buffer { buffer -> String in
            String(decoding: buffer, as: UTF8.self)
        }
        self.init(string: string)
    }

    var description: String {
        return string
    }

    var debugDescription: String {
        return string
    }
}

extension StringType where Self: Encodable {

    func encode(to encoder: Encoder) throws {
        var container = encoder.singleValueContainer()
        try container.encode(string)
    }
}

extension StringType where Self: Decodable {

    init(from decoder: Decoder) throws {
        let string = try decoder.singleValueContainer().decode(String.self)
        self.init(string: string)
    }
}
