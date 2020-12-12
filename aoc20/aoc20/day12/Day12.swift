//
//  Day12.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-12.
//

import Foundation

struct Day12: Day {

    struct Ferry {
        var position: Point = .origin
        var direction: Vector = .east
        var waypoint: Vector = .init(dx: 10, dy: 1)

        mutating func applyFirst(instruction: Instruction) {
            switch instruction.action {
            case .north:
                position = position.offset(by: Vector.north.times(instruction.value))
            case .south:
                position = position.offset(by: Vector.south.times(instruction.value))
            case .east:
                position = position.offset(by: Vector.east.times(instruction.value))
            case .west:
                position = position.offset(by: Vector.west.times(instruction.value))
            case .left:
                direction = direction.rotated(byDegrees: -instruction.value)
            case .right:
                direction = direction.rotated(byDegrees: instruction.value)
            case .forward:
                position = position.offset(by: direction.times(instruction.value))
            }
        }

        mutating func applySecond(instruction: Instruction) {
            switch instruction.action {
            case .north:
                waypoint = waypoint.adding(Vector.north.times(instruction.value))
            case .south:
                waypoint = waypoint.adding(Vector.south.times(instruction.value))
            case .east:
                waypoint = waypoint.adding(Vector.east.times(instruction.value))
            case .west:
                waypoint = waypoint.adding(Vector.west.times(instruction.value))
            case .left:
                waypoint = waypoint.rotated(byDegrees: -instruction.value)
            case .right:
                waypoint = waypoint.rotated(byDegrees: instruction.value)
            case .forward:
                position = position.offset(by: waypoint.times(instruction.value))
            }
        }
    }

    struct Instruction: PatternConvertible {
        static let pattern: String = "(.)(\\d+)"

        var action: Action
        var value: Int

        init(match: PatternMatch) {
            action = match.parse(Action.self, at: 0)
            value = match.int(at: 1)
        }
    }

    enum Action: Character {
        case north = "N", south = "S", east = "E", west = "W"
        case left = "L", right = "R"
        case forward = "F"
    }

    static func first() -> String {
        let instructions = day12_input.lines(Instruction.self)
        var ferry = Ferry()
        instructions.forEach { i in
            ferry.applyFirst(instruction: i)
        }
        let manhattanDistance = abs(ferry.position.x) + abs(ferry.position.y)
        return manhattanDistance.description
    }

    static func second() -> String {
        let instructions = day12_input.lines(Instruction.self)
        var ferry = Ferry()
        instructions.forEach { i in
            ferry.applySecond(instruction: i)
        }
        let manhattanDistance = abs(ferry.position.x) + abs(ferry.position.y)
        return manhattanDistance.description
    }
}

extension Vector {
    fileprivate static let north: Vector = .init(dx: 0, dy: 1)
    fileprivate static let east: Vector = .init(dx: 1, dy: 0)
    fileprivate static let south: Vector = .init(dx: 0, dy: -1)
    fileprivate static let west: Vector = .init(dx: -1, dy: 0)
}
