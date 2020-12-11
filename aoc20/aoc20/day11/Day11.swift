//
//  Day11.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-11.
//

import Foundation

struct Day11: Day {

    struct Map {
        var squares: [[Square]]

        var occupiedCount: Int {
            squares.lazy.flatMap { $0 }
                .countWhere { $0 == .occupied }
        }

        var grid: Grid {
            .init(min: .init(x: 0, y: 0), max: .init(x: squares[0].count - 1, y: squares.count - 1))
        }

        init(lines: [String]) {
            self.squares = lines.map { $0.compactMap(Square.init(rawValue:)) }
        }

        subscript(point: Point) -> Square {
            get { squares[point.y][point.x] }
            set { squares[point.y][point.x] = newValue }
        }

        func occupied(adjacentTo point: Point) -> Int {
            return Grid(min: point.offset(x: -1, y: -1), max: point.offset(x: 1, y: 1))
                .filter { $0 != point }
                .countWhere { p -> Bool in
                    let square = squares[safe: p.y]?[safe: p.x] ?? .floor
                    return square == .occupied
                }
        }

        func occupied(asCanBeSeenFrom point: Point) -> Int {
            let directions: [Vector] = [
                .init(dx: 0, dy: 1),
                .init(dx: 1, dy: 1),
                .init(dx: 1, dy: 0),
                .init(dx: 1, dy: -1),
                .init(dx: 0, dy: -1),
                .init(dx: -1, dy: -1),
                .init(dx: -1, dy: 0),
                .init(dx: -1, dy: 1),
            ]
            return directions.countWhere { direction -> Bool in
                let seat = walk(from: point, inDirection: direction).lazy
                    .map { self[$0] }.first(where: { $0 != .floor })
                return seat == .occupied
            }
        }

        private func walk(from start: Point, inDirection direction: Vector) -> AnySequence<Point> {
            var next = start
            let grid = self.grid
            return AnySequence {
                AnyIterator {
                    next = next.offset(by: direction)
                    return grid.contains(next) ? next : nil
                }
            }
        }
    }

    enum Square: Character {
        case vacant = "L", occupied = "#", floor = "."
    }

    static func first() -> String {
        var map = Map(lines: day11_input.lines())

        var occupiedCount = 0
        repeat {
            occupiedCount = map.occupiedCount
            firstSimulateOnce(in: &map)
        } while occupiedCount != map.occupiedCount

        return occupiedCount.description
    }

    private static func firstSimulateOnce(in map: inout Map) {
        var changes: [(Point, Square)] = []
        map.grid.forEach { point in
            let square = map[point]
            let occupied = map.occupied(adjacentTo: point)
            if square == .vacant && occupied == 0 {
                changes.append((point, .occupied))
            }
            if square == .occupied && occupied >= 4 {
                changes.append((point, .vacant))
            }
        }
        changes.forEach { point, square in
            map[point] = square
        }
    }

    static func second() -> String {
        var map = Map(lines: day11_input.lines())

        var occupiedCount = 0
        repeat {
            occupiedCount = map.occupiedCount
            secondSimulateOnce(in: &map)
        } while occupiedCount != map.occupiedCount

        return occupiedCount.description
    }

    private static func secondSimulateOnce(in map: inout Map) {
        var changes: [(Point, Square)] = []
        map.grid.forEach { point in
            let square = map[point]
            let occupied = map.occupied(asCanBeSeenFrom: point)
            if square == .vacant && occupied == 0 {
                changes.append((point, .occupied))
            }
            if square == .occupied && occupied >= 5 {
                changes.append((point, .vacant))
            }
        }
        changes.forEach { point, square in
            map[point] = square
        }
    }

}

extension Day11.Map: CustomStringConvertible {

    var description: String {
        squares.map { String($0.map { $0.rawValue }) }
            .joined(separator: "\n")
    }
}
