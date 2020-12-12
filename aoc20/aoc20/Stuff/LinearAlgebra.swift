//
//  LinearAlgebra.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-07.
//

import Foundation

struct Point: Hashable {

    static let origin: Point = .init(x: 0, y: 0)

    var x: Int
    var y: Int

    func offset(x: Int = 0, y: Int = 0) -> Point {
        return .init(x: self.x + x, y: self.y + y)
    }

    func offset(by vector: Vector) -> Point {
        return .init(x: x + vector.dx, y: y + vector.dy)
    }
}

struct Vector: Hashable {
    var dx: Int
    var dy: Int

    func times(_ magnitude: Int) -> Vector {
        .init(dx: dx * magnitude, dy: dy * magnitude)
    }

    func adding(_ other: Vector) -> Vector {
        .init(dx: dx + other.dx, dy: dy + other.dy)
    }

    func rotated(byDegrees degrees: Int) -> Vector {
        switch degrees {
        case 0:
            return self
        case 90, -270:
            return .init(dx: dy, dy: -dx)
        case 180, -180:
            return .init(dx: -dx, dy: -dy)
        case 270, -90:
            return .init(dx: -dy, dy: dx)
        default:
            fatalError("Unsupported degrees: \(degrees)")
        }
    }
}

struct Grid: Equatable, Sequence {
    var min: Point
    var max: Point

    @inlinable
    func contains(_ point: Point) -> Bool {
        return point.x >= min.x && point.x <= max.x
            && point.y >= min.y && point.y <= max.y
    }

    @inlinable
    func makeIterator() -> Iterator {
        return Iterator(min: min, max: max, current: min)
    }

    struct Iterator: IteratorProtocol {
        let min: Point
        let max: Point
        var current: Point

        @inlinable
        mutating func next() -> Point? {
            let next = current
            if next.y > max.y { return nil }
            current.x += 1
            if current.x > max.x {
                current.x = min.x
                current.y += 1
            }
            return next
        }
    }
}

extension Point: CustomStringConvertible {
    var description: String {
        "(x: \(x), y: \(y))"
    }
}

extension Vector: CustomStringConvertible {
    var description: String {
        "(dx: \(dx), dy: \(dy))"
    }
}
