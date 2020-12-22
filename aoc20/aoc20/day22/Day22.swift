//
//  Day21.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-22.
//

import Foundation

struct Day22: Day {

    enum Player {
        case one, two
    }

    struct HistoryEntry: Hashable {
        var deck1: [Int]
        var deck2: [Int]
    }

    static func first() -> String {
        var (deck1, deck2) = parseDecks()
        playCombat(deck1: &deck1, deck2: &deck2)

        let winner = deck1.count > deck2.count ? deck1 : deck2
        let score = winner.reversed().enumerated().reduce(0) { acc, pair -> Int in
            let (index, card) = pair
            return acc + (index+1) * card
        }

        return score.description
    }

    private static func playCombat(deck1: inout [Int], deck2: inout [Int]) {
        while !deck1.isEmpty && !deck2.isEmpty {
            let card1 = deck1.removeFirst()
            let card2 = deck2.removeFirst()
            assert(card1 != card2)
            if card1 > card2 { deck1.append(contentsOf: [card1, card2]) }
            if card1 < card2 { deck2.append(contentsOf: [card2, card1]) }
        }
    }

    static func second() -> String {
        var (deck1, deck2) = parseDecks()

        let winningDeck: [Int]
        switch playRecursiveCombat(deck1: &deck1, deck2: &deck2) {
        case .one: winningDeck = deck1
        case .two: winningDeck = deck2
        }

        let score = winningDeck.reversed().enumerated().reduce(0) { acc, pair -> Int in
            let (index, card) = pair
            return acc + (index+1) * card
        }

        return score.description
    }

    private static func playRecursiveCombat(deck1: inout [Int], deck2: inout [Int]) -> Player {
        var history: [HistoryEntry] = []

        while !deck1.isEmpty && !deck2.isEmpty {
            let entry = HistoryEntry(deck1: deck1, deck2: deck2)
            if history.contains(entry) { return .one }
            history.append(entry)

            let card1 = deck1.removeFirst()
            let card2 = deck2.removeFirst()
            assert(card1 != card2)

            var roundWinner: Player
            if card1 <= deck1.count && card2 <= deck2.count {
                var subdeck1 = Array(deck1.prefix(card1))
                var subdeck2 = Array(deck2.prefix(card2))
                roundWinner = playRecursiveCombat(deck1: &subdeck1, deck2: &subdeck2)
            } else if card1 > card2 {
                roundWinner = .one
            } else {
                roundWinner = .two
            }

            switch roundWinner {
            case .one: deck1.append(contentsOf: [card1, card2])
            case .two: deck2.append(contentsOf: [card2, card1])
            }
        }
        return deck1.count > deck2.count ? .one : .two
    }

    private static func parseDecks() -> ([Int], [Int]) {
        let parts = day22_input.lines(separator: "\n\n")
        let p1String = parts[0]
        let p2String = parts[1]

        let deck1 = p1String.components(separatedBy: "\n").dropFirst()
            .compactMap(Int.init)
        let deck2 = p2String.components(separatedBy: "\n").dropFirst()
            .compactMap(Int.init)
        return (deck1, deck2)
    }
}
