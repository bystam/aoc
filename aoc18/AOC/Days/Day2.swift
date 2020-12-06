//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

final class Day2: Solver {

    private struct ChecksumParts {
        var twos: Int = 0
        var threes: Int = 0

        var checksum: Int { return twos * threes }
    }

    func solveFirst(input: Input) throws -> String {
        return input.lines()
            .map { line in line.countedOccurrences() }
            .reduce(into: ChecksumParts(), { acc, lineCharacterCount in
                if lineCharacterCount.values.contains(2) {
                    acc.twos += 1
                }
                if lineCharacterCount.values.contains(3) {
                    acc.threes += 1
                }
            })
            .checksum
            .description
    }

    func solveSecond(input: Input) throws -> String {
        let lines = input.lines()
        for line1 in lines {
            for line2 in lines {
                let lcs = line1.lcs(with: line2)
                if lcs.count == line1.count - 1 {
                    return String(lcs)
                }
            }
        }
        fatalError()
    }
}
