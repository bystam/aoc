//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

enum Algorithms {

    private enum PointOrientation {
        case colinear, clockwise, counterClockwise
    }

    static func boundingRect<C: Collection>(of points: C) -> Rect where C.Element == Point {
        precondition(!points.isEmpty)
        var topLeft = points.first!
        var bottomRight = points.first!
        points.dropFirst().forEach { p in
            topLeft.x = min(topLeft.x, p.x)
            topLeft.y = min(topLeft.y, p.y)
            bottomRight.x = max(bottomRight.x, p.x)
            bottomRight.y = max(bottomRight.y, p.y)
        }
        return Rect(x: topLeft.x, y: topLeft.y,
                    width: (bottomRight.x - topLeft.x), height: bottomRight.y - topLeft.y)
    }

    static func convexHull<C: Collection>(of points: C) -> Set<Point> where C.Element == Point {
        guard points.count >= 3 else { return Set(points) }

        let leftMost = points.min(by: { p1, p2 in p1.x < p2.x })!
        var hull: Set<Point> = Set()

        let leftMostIndex = points.firstIndex(of: leftMost)!
        var p = leftMostIndex
        repeat {
            hull.insert(points[p])

            var q = points.index(after: p)
            if !points.indices.contains(q) {
                q = points.startIndex
            }
            points.indices.forEach { i in
                if orientation(points[p], points[i], points[q]) == .counterClockwise {
                    q = i
                }
            }

            p = q

        } while p != leftMostIndex

        return hull
    }

    private static func orientation(_ p: Point, _ q: Point, _ r: Point) -> PointOrientation {
        let val: Int = (q.y - p.y) * (r.x - q.x)
            - (q.x - p.x) * (r.y - q.y)

        if val == 0 {
            return .colinear
        } else if val > 0 {
            return .clockwise
        } else {
            return .counterClockwise
        }
    }
}
