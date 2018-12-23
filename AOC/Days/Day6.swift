//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

final class Day6: Solver {

    private typealias Coordinate = Point
    private typealias Location = Point

    func solveFirst(input: Input) throws -> String {

        let points = Set(input.lines().map(Point.init))
        let hull = Algorithms.convexHull(of: points)
        let rect = Algorithms.boundingRect(of: points)

        return points
            .subtracting(hull)
            .map { p -> Int in
                let other = points.subtracting([p])
                return measureReach(from: p, within: rect, untilInsideDomainOf: other)
            }
            .max()!
            .description
    }

    func solveSecond(input: Input) throws -> String {
        return ""
    }

    private func measureReach(from start: Coordinate, within rect: Rect, untilInsideDomainOf other: Set<Point>) -> Int {
        var visited: Set<Point> = []
        var queue: [Point] = [start]
        while !queue.isEmpty {
            let p = queue.removeFirst()

            if !rect.contains(p) || visited.contains(p) {
                continue
            }

            let endReached = other.contains(where: { otherCoordinate in
                return otherCoordinate.manhattanDistance(to: p) <= start.manhattanDistance(to: p)
            })
            if endReached {
                continue
            }

            visited.insert(p)

            let up = p.offset(x: 0, y: -1)
            let down = p.offset(x: 0, y: 1)
            let right = p.offset(x: 1, y: 0)
            let left = p.offset(x: -1, y: 0)
            if !visited.contains(up) {
                queue.append(up)
            }
            if !visited.contains(down) {
                queue.append(down)
            }
            if !visited.contains(right) {
                queue.append(right)
            }
            if !visited.contains(left) {
                queue.append(left)
            }
        }

        return visited.count
    }
}

private let regex = Regex("(\\d+), (\\d+)")

private extension Point {

    init(string: String) {
        let match = regex.matchOne(in: string)!
        self.init(x: match.capturedValue(at: 1)!,
                  y: match.capturedValue(at: 2)!)
    }
}
