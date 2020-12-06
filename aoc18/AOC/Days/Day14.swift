//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

final class Day14: Solver {

    fileprivate struct Recipe {
        let score: Int8

        func combine(with other: Recipe) -> [Recipe] {
            let total = score + other.score
            if total >= 10 {
                return [total / 10, total % 10].map(Recipe.init)
            }
            return [Recipe(score: total)]
        }
    }

    func solveFirst(input: Input) throws -> String {
        let goalCount = input.int()

        var current1: Int = 0
        var current2: Int = 1
        var recipes: [Recipe] = [Recipe(score: 3), Recipe(score: 7)]

        while recipes.count < (goalCount + 10) {
            let r1 = recipes[current1]
            let r2 = recipes[current2]
            recipes += r1.combine(with: r2)

            current1 = (current1 + Int(r1.score) + 1) % recipes.count
            current2 = (current2 + Int(r2.score) + 1) % recipes.count
        }

        return recipes[goalCount...]
            .map { $0.score.description }
            .joined()
    }

    func solveSecond(input: Input) throws -> String {
        let targetSequence = input.string()

        var current1: Int = 0
        var current2: Int = 1
        var recipes: [Recipe] = [Recipe(score: 3), Recipe(score: 7)]

        while true {
            let r1 = recipes[current1]
            let r2 = recipes[current2]
            recipes += r1.combine(with: r2)

            current1 = (current1 + Int(r1.score) + 1) % recipes.count
            current2 = (current2 + Int(r2.score) + 1) % recipes.count

            let end1 = recipes.suffix(targetSequence.count + 1).dropLast().map { $0.score.description }.joined()
            if end1 == targetSequence {
                recipes.removeLast()
                break
            }
            let end2 = recipes.suffix(targetSequence.count).map { $0.score.description }.joined()
            if end2 == targetSequence {
                break
            }
        }

        return (recipes.count - targetSequence.count).description
    }
}
