//
//  Day24.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-24.
//

import Foundation

struct Day24: Day {

    struct Walk {
        var steps: [Direction] = []
    }

    enum Direction: CaseIterable {
        case east, southEast, southWest, west, northWest, northEast
    }

    static func first() -> String {
        return initiallyFlipped().count.description
    }

    static func second() -> String {
        var blackTiles = initiallyFlipped()

        for _ in (0..<100) {
            var blackNeighbors: [Point: Int] = [:]

            for tile in blackTiles {
                blackNeighbors[tile] = 0
            }
            for tile in blackTiles {
                for neighbor in tile.neighbors {
                    blackNeighbors[neighbor, default: 0] += 1
                }
            }

            for (point, blackNeighborCount) in blackNeighbors {
                let isBlack = blackTiles.contains(point)
                switch (isBlack, blackNeighborCount) {
                case (true, 0):
                    blackTiles.remove(point)
                case (true, 3...):
                    blackTiles.remove(point)
                case (false, 2):
                    blackTiles.insert(point)
                default:
                    break
                }
            }
        }
        return blackTiles.count.description
    }

    private static func initiallyFlipped() -> Set<Point> {
        let walks = day24_input.lines(Walk.init(string:))
        var flipped: Set<Point> = []

        for walk in walks {
            var point: Point = .origin
            for step in walk.steps {
                point = point.offset(by: step)
            }
            if flipped.contains(point) {
                flipped.remove(point)
            } else {
                flipped.insert(point)
            }
        }
        return flipped
    }
}

extension Day24.Walk {
    init(string: String) {
        var index = string.startIndex
        while index < string.endIndex {
            switch string[index] {
            case "n":
                let next = string[string.index(after: index)]
                if next == "e" { steps.append(.northEast) }
                else if next == "w" { steps.append(.northWest) }
                else { fatalError() }
                index = string.index(index, offsetBy: 2)
            case "s":
                let next = string[string.index(after: index)]
                if next == "e" { steps.append(.southEast) }
                else if next == "w" { steps.append(.southWest) }
                else { fatalError() }
                index = string.index(index, offsetBy: 2)
            case "e":
                steps.append(.east)
                index = string.index(index, offsetBy: 1)
            case "w":
                steps.append(.west)
                index = string.index(index, offsetBy: 1)
            default:
                fatalError()
            }
        }
    }
}

fileprivate extension Point {

    var neighbors: [Point] {
        Day24.Direction.allCases.map { offset(by: $0) }
    }

    func offset(by direction: Day24.Direction) -> Point {
        switch direction {
        case .east:
            return offset(x: 2, y: 0)
        case .southEast:
            return offset(x: 1, y: 1)
        case .southWest:
            return offset(x: -1, y: 1)
        case .west:
            return offset(x: -2, y: 0)
        case .northWest:
            return offset(x: -1, y: -1)
        case .northEast:
            return offset(x: 1, y: -1)
        }
    }
}
