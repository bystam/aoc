//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

final class Regex {

    struct Match {
        fileprivate let source: String
        fileprivate let result: NSTextCheckingResult
    }

    private let inner: NSRegularExpression

    init(_ string: StaticString) {
        self.inner = try! NSRegularExpression(pattern: string.description, options: [.caseInsensitive])
    }

    func matchOne(in string: String) -> Match? {
        let result = inner.firstMatch(in: string, options: [], range: NSMakeRange(0, string.utf16.count))
        return result.map { Match(source: string, result: $0) }
    }
}

extension Regex.Match {

    func capturedValue<T: LosslessStringConvertible>(at index: Int, type: T.Type) -> T? {
        return capturedValue(at: index)
    }

    func capturedValue<T: LosslessStringConvertible>(at index: Int) -> T? {
        guard (0..<result.numberOfRanges).contains(index) else { return nil }
        let range = result.range(at: index)
        let start = String.UTF16Index(encodedOffset: range.location)
        let end = String.UTF16Index(encodedOffset: range.location + range.length)

        var matchedString = String(source.utf16[start..<end])
        if T.self == Int.self {
            matchedString = matchedString?.trimmingCharacters(in: .whitespaces)
        }

        return matchedString.flatMap(T.init)
    }
}
