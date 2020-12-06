//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

private final class Marble {
    let number: Int
    var next: Marble!
    var previous: Marble!

    static func root() -> Marble {
        let root = Marble(number: 0)
        root.next = root
        root.previous = root
        return root
    }

    private init(number: Int) {
        self.number = number
    }

    init(number: Int, next: Marble, previous: Marble) {
        self.number = number
        self.next = next
        self.previous = previous
    }

    func clockwise(steps: Int) -> Marble {
        return (0..<steps).reduce(self, { acc, _ -> Marble in
            return acc.next
        })
    }

    func counterClockwise(steps: Int) -> Marble {
        return (0..<steps).reduce(self, { acc, _ -> Marble in
            return acc.previous
        })
    }
}

private let regex = Regex("(.+) players; last marble is worth (.+) points")

private struct Game {
    let players: Int
    let marbleCount: Int
}

extension Game {
    init(string: String) {
        let match = regex.matchOne(in: string)!
        self.init(players: match.capturedValue(at: 1)!, marbleCount: match.capturedValue(at: 2)!)
    }
}

private final class Circle {
    var current: Marble

    init(marble: Marble) {
        self.current = marble
    }
}

final class Day9: Solver {

    private typealias Player = Int
    private typealias Score = Int

//    var mockInput: String? {
//        return "9 players; last marble is worth 25 points"
//    }

    func solveFirst(input: Input) throws -> String {
        let game = Game(string: input.string())
        return playGame(game).description
    }

    func solveSecond(input: Input) throws -> String {
        let game = Game(string: input.string())
        let modified = Game(players: game.players, marbleCount: 100 * game.marbleCount)
        return playGame(modified).description
    }

    private func playGame(_ game: Game) -> Int {
        var scores: [Player: Score] = [:]

        var currentPlayer: Player = 0
        var currentMarble: Marble = Marble.root()

        (1..<game.marbleCount).forEach { number in
            if (number % 23) == 0 {
                let sevenBack = currentMarble.counterClockwise(steps: 7)
                let before = sevenBack.counterClockwise(steps: 1)
                let after = sevenBack.clockwise(steps: 1)
                before.next = after
                after.previous = before
                currentMarble = after

                scores[currentPlayer, default: 0] += (number + sevenBack.number)
            } else {
                let oneAway = currentMarble.clockwise(steps: 1)
                let twoAway = currentMarble.clockwise(steps: 2)
                let marble = Marble(number: number, next: twoAway, previous: oneAway)
                oneAway.next = marble
                twoAway.previous = marble
                currentMarble = marble
            }

            currentPlayer = (currentPlayer + 1) % game.players
        }

        return scores
            .values
            .max()!
    }
}
