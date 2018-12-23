//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

final class Day1: Solver {

    func solveFirst(input: Input) throws -> String {
        return input.lines()
            .compactMap(Int.init)
            .reduce(0, +)
            .description
    }

    func solveSecond(input: Input) throws -> String {
        var seen: Set<Int> = []
        return input.lines()
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
