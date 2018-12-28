//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

struct Point: Hashable, CustomStringConvertible {
    var x: Int
    var y: Int

    static let zero: Point = Point(x: 0, y: 0)

    var asVector: Vector {
        return Vector(dx: x, dy: y)
    }

    init(x: Int, y: Int) {
        self.x = x
        self.y = y
    }

    func offsetBy(x: Int, y: Int) -> Point {
        return Point(x: self.x + x, y: self.y + y)
    }

    func manhattanDistance(to other: Point) -> Int {
        return abs(x - other.x) + abs(y - other.y)
    }

    func applying(_ vector: Vector) -> Point {
        return Point(x: x + vector.dx, y: y + vector.dy)
    }

    var description: String {
        return "Point{x: \(x), y: \(y)"
    }
}

struct Vector: Hashable, CustomStringConvertible {
    var dx: Int
    var dy: Int

    var inverted: Vector {
        return Vector(dx: -dx, dy: -dy)
    }

    init(dx: Int, dy: Int) {
        self.dx = dx
        self.dy = dy
    }

    func rotatedClockwise() -> Vector {
        return Vector(dx: -dy, dy: dx)
    }

    var description: String {
        return "Vector{dx: \(dx), dy: \(dy)"
    }
}

struct Size: Hashable {
    var width: Int
    var height: Int

    var area: Int {
        return width * height
    }

    func expandedBy(dx: Int, dy: Int) -> Size {
        return Size(width: width + dx, height: height + dy)
    }
}

struct Rect: Hashable {
    var origin: Point
    var size: Size

    var topLeft: Point { return origin }
    var topRight: Point { return Point(x: origin.x + size.width, y: origin.y) }
    var bottomLeft: Point { return Point(x: origin.x, y: origin.y + size.height) }
    var bottomRight: Point { return Point(x: origin.x + size.width, y: origin.y + size.height) }

    func contains(_ point: Point) -> Bool {
        return point.x >= origin.x
            && point.y >= origin.y
            && point.x < (origin.x + size.width)
            && point.y < (origin.y + size.height)
    }

    func insetBy(dx: Int, dy: Int) -> Rect {
        return Rect(x: origin.x - dx, y: origin.y - dy,
                    width: size.width + 2*dx, height: size.height + 2*dy)
    }

    func border() -> AnySequence<Point> {
        var point = topLeft
        var direction = Vector(dx: 1, dy: 0)
        var isFirst = true
        return AnySequence<Point> { () -> AnyIterator<Point> in
            return AnyIterator<Point> { () -> Point? in
                let next = point

                if next == self.topLeft && !isFirst {
                    return nil
                }
                isFirst = false

                point = next.applying(direction)
                if !self.contains(point) {
                    direction = direction.rotatedClockwise()
                    point = next.applying(direction)
                }

                return next
            }
        }
    }

    func byColumn() -> AnySequence<Point> {
        let origin = self.origin
        let size = self.size
        return AnySequence { () -> AnyIterator<Point> in
            let xRange = (origin.x..<origin.x + size.width)
            let yRange = (origin.y..<origin.y + size.height)
            var p = origin
            return AnyIterator { () -> Point? in
                guard xRange.contains(p.x) && yRange.contains(p.y) else { return nil }
                let point = p
                p.y += 1
                if !yRange.contains(p.y) {
                    p.y = yRange.startIndex
                    p.x += 1
                }
                return point
            }
        }
    }

    func byRow() -> AnySequence<Point> {
        let origin = self.origin
        let size = self.size
        return AnySequence { () -> AnyIterator<Point> in
            let xRange = (origin.x..<origin.x + size.width)
            let yRange = (origin.y..<origin.y + size.height)
            var p = origin
            return AnyIterator { () -> Point? in
                guard xRange.contains(p.x) && yRange.contains(p.y) else { return nil }
                let point = p
                p.x += 1
                if !xRange.contains(p.x) {
                    p.x = xRange.startIndex
                    p.y += 1
                }
                return point
            }
        }
    }
}

extension Rect {
    init(x: Int, y: Int, width: Int, height: Int) {
        self.init(origin: .init(x: x, y: y), size: .init(width: width, height: height))
    }
}

struct Matrix<T> {

    var width: Int {
        return inner.count
    }

    var height: Int {
        return inner[0].count
    }

    private var inner: [[T]]

    init(width: Int, height: Int, initialValue: T) {
        assert(width > 0 && height > 0)
        inner = [[T]](repeating: [T](repeating: initialValue, count: height), count: width)
    }

    init(size: Size, initialValue: T) {
        self.init(width: size.width, height: size.height, initialValue: initialValue)
    }

    subscript(x x: Int, y y: Int) -> T {
        get {
            return inner[x][y]
        }
        set {
            inner[x][y] = newValue
        }
    }
}

extension Matrix: Sequence {

    func makeIterator() -> AnyIterator<T> {
        let xRange = (0..<width)
        let yRange = (0..<height)
        var p = Point(x: 0, y: 0)
        return AnyIterator { () -> T? in
            guard xRange.contains(p.x) && yRange.contains(p.y) else { return nil }
            let value = self[x: p.x, y: p.y]
            p.y += 1
            if !yRange.contains(p.y) {
                p.y = yRange.startIndex
                p.x += 1
            }
            return value
        }
    }
}

