//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

final class Day12: Solver {

    fileprivate typealias PotID = Int

    fileprivate enum PotContent: Character {
        case empty = ".", plant = "#"
    }

    fileprivate struct Note {
        let input: [PotContent]
        let output: PotContent

        func matches(pot id: PotID, in generation: Generation) -> Bool {
            return generation.content(at: id - 2) == input[0]
                && generation.content(at: id - 1) == input[1]
                && generation.content(at: id + 0) == input[2]
                && generation.content(at: id + 1) == input[3]
                && generation.content(at: id + 2) == input[4]
        }
    }

    fileprivate struct Generation {
        let offset: PotID
        let pots: [PotContent]

        init(offset: PotID, pots: [PotContent]) {
            self.offset = offset
            self.pots = pots
        }

        init(pots: [PotID: PotContent]) {
            let start = pots.keys.min()!
            let end = pots.keys.max()!
            let range = (start...end)

            self.init(offset: start, pots: Array(range
                .map { pots[$0] ?? .empty }
                .drop(while: { $0 == .empty })
            ))
        }

        var reproductiveReach: ClosedRange<PotID> {
            return (offset - 2)...(offset + pots.count + 2)
        }

        func content(at id: PotID) -> PotContent {
            let id = id - offset
            return pots.indices.contains(id) ? pots[id] : .empty
        }

        func plantPotIDs() -> Set<PotID> {
            return Set(pots.indices.compactMap { potId -> PotID? in
                pots[potId] == .plant ? (potId + offset) : nil }
            )
        }

        func relativePlantPotIDs() -> Set<PotID> {
            return Set(pots.indices.compactMap { potId -> PotID? in
                pots[potId] == .plant ? potId : nil }
            )
        }
    }

    func solveFirst(input: Input) throws -> String {
        let lines = input.lines(includingEmpty: true)
        let initial = Generation(string: lines[0])
        let notes = lines[2...].map(Note.init).filter { $0.output == .plant }

        return simulateGenerations(count: 20, from: initial, with: notes)
            .plantPotIDs()
            .reduce(0, +)
            .description
    }

    func solveSecond(input: Input) throws -> String {
        let lines = input.lines(includingEmpty: true)
        let initial = Generation(string: lines[0])
        let notes = lines[2...].map(Note.init).filter { $0.output == .plant }

        return simulateGenerations(count: 50000000000, from: initial, with: notes)
            .plantPotIDs()
            .reduce(0, +)
            .description
    }

    private func simulateGenerations(count: Int, from initial: Generation, with notes: [Note]) -> Generation {
        var previous = initial

        var relativeSteadyStateOffset: Int? = nil
        var time = 0
        while time < count {
            var pots: [PotID: PotContent] = [:]

            for potId in previous.reproductiveReach {
                if let note =  notes.first(where: { $0.matches(pot: potId, in: previous) }) {
                    pots[potId] = note.output
                }
            }

            let next = Generation(pots: pots)
            if next.relativePlantPotIDs() == previous.relativePlantPotIDs() {
                relativeSteadyStateOffset = next.offset - previous.offset
                break
            }

            previous = next
            time += 1
        }

        if let steadyStateOffset = relativeSteadyStateOffset {
            let remainingGenerations = (count - time)
            let totalOffset = previous.offset + remainingGenerations * steadyStateOffset
            previous = Generation(offset: totalOffset, pots: previous.pots)
        }

        return previous
    }
}

private extension Day12.Generation {

    init(string: String) {
        let pots = string
            .replacingOccurrences(of: "initial state: ", with: "")
            .enumerated()
            .reduce(into: [Day12.PotID: Day12.PotContent](), { pots, pair in
                pots[pair.offset] = Day12.PotContent(rawValue: pair.element)!
            })
        self.init(pots: pots)
    }
}

private extension Day12.Note {

    init(string: String) {
        let inputRaw = string.components(separatedBy: " => ")
        self.init(input: inputRaw[0].compactMap(Day12.PotContent.init),
                  output: Day12.PotContent(rawValue: inputRaw[1].first!)!)
    }
}
