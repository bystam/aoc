//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

struct Point: Hashable {
    var x: Int
    var y: Int
}

struct Size: Hashable {
    var width: Int
    var height: Int
}

struct Rect: Hashable {
    var origin: Point
    var size: Size
}

extension Rect {
    init(x: Int, y: Int, width: Int, height: Int) {
        self.init(origin: .init(x: x, y: y), size: .init(width: width, height: height))
    }
}

extension Rect: Sequence {

    func makeIterator() -> AnyIterator<Point> {
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

