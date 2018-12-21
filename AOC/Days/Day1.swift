//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

final class Day1: Solver {

    func solveFirst(input lines: [String]) throws -> String {
        let sum: Int = lines
            .compactMap(Int.init)
            .reduce(0, +)
        return String(sum)
    }

    func solveSecond(input lines: [String]) throws -> String {
        var seen: Set<Int> = []
        return lines
            .infinatelyRepeated()
            .lazy
            .map(parseInt)
            .scan(0, +)
            .first(where: { freq -> Bool in
                let (inserted, _) = seen.insert(freq)
                return !inserted
            })!
            .description
    }
}
