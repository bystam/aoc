//
//  Day4.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-07.
//

import Foundation

struct Day4: Day {

    enum Field: String, CaseIterable {
        case birthYear = "byr"
        case issueYear = "iyr"
        case expirationYear = "eyr"
        case height = "hgt"
        case hairColor = "hcl"
        case eyeColor = "ecl"
        case passportID = "pid"
        case countryID = "cid"

        func isValid(for value: String) -> Bool {
            switch self {
            case .birthYear:
                return value.count == 4 && value >= "1920" && value <= "2002"
            case .issueYear:
                return value.count == 4 && value >= "2010" && value <= "2020"
            case .expirationYear:
                return value.count == 4 && value >= "2020" && value <= "2030"
            case .height:
                if value.hasSuffix("cm") {
                    let number = value.dropLast(2)
                    return number >= "150" && number <= "193"
                }
                if value.hasSuffix("in") {
                    let number = value.dropLast(2)
                    return number >= "59" && number <= "76"
                }
                return false
            case .hairColor:
                return value.matches(pattern: "#[a-f0-9]{6}")
            case .eyeColor:
                return ["amb", "blu", "brn", "gry", "grn", "hzl", "oth"]
                    .contains(value)
            case .passportID:
                return value.matches(pattern: "[0-9]{9}")
            case .countryID:
                return true
            }
        }
    }

    struct Document {
        var data: [Field: String]

        init(string: String) {
            data = string.replacingOccurrences(of: "\n", with: " ")
                .components(separatedBy: " ")
                .reduce(into: [:]) { acc, raw in
                    let entry = raw.components(separatedBy: ":")
                    let field = Field(rawValue: entry[0])!
                    let value = entry[1]
                    acc[field] = value
                }
        }

        var isValidEnough: Bool {
            let required = Set(Field.allCases).subtracting([.countryID])
            let fields = Set(data.keys)
            return fields.isSuperset(of: required)
        }

        var isDeeplyValid: Bool {
            return isValidEnough && data.reduce(true) { acc, pair in
                acc && pair.key.isValid(for: pair.value)
            }
        }
    }

    static func first() -> String {
        let documents = day4_input.lines(separator: "\n\n").map(Document.init)
        return documents
            .countWhere(\.isValidEnough)
            .description
    }

    static func second() -> String {

        let documents = day4_input.lines(separator: "\n\n").map(Document.init)

        return documents
            .countWhere(\.isDeeplyValid)
            .description
    }
}

private extension String {
    func matches(pattern: String) -> Bool {
        let expression = try! NSRegularExpression(
            pattern: "^\(pattern)$", options: []
        )
        return expression.matches(
            in: self, options: [],
            range: NSRange(location: 0, length: self.utf16.count)
        ).count == 1
    }
}
