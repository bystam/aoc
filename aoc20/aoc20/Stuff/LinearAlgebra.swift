//
//  LinearAlgebra.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-07.
//

import Foundation

struct Point: Hashable {
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
