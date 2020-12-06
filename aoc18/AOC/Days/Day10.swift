//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

final class Day10: Solver {

    fileprivate struct MovingPoint {
        var point: Point
        let velocity: Vector

        mutating func move() {
            point = point.applying(velocity)
        }
    }

    func solveFirst(input: Input) throws -> String {
        var points = input
            .lines()
            .map(MovingPoint.init)

        var candidates: [String] = []
        for _ in (0..<20000) {
            let snapshot = Set(points.map(^\.point))
            let alignment = snapshot.grouped(by: ^\.y).values.map(^\.count).max()!

            if alignment > 15 {
                candidates.append(stringify(points: snapshot))
            }

            if candidates.count > 20 {
                break
            }

            (0..<points.count).forEach { points[$0].move() }
        }

        return "candidates.count = \(candidates.count)\n\(candidates.joined(separator: "\n\n"))"
    }

    func solveSecond(input: Input) throws -> String {
        var points = input
            .lines()
            .map(MovingPoint.init)

        var candidates: [String] = []
        for i in (0..<20000) {
            let snapshot = Set(points.map(^\.point))
            let alignment = snapshot.grouped(by: ^\.y).values.map(^\.count).max()!

            if alignment > 15 {
                candidates.append(stringify(points: snapshot))
            }

            if candidates.count == 4 {
                return i.description
            }

            (0..<points.count).forEach { points[$0].move() }
        }

        fatalError()
    }

    private func stringify(points: Set<Point>) -> String {
        var rect = Algorithms.boundingRect(of: points)
        rect.size = rect.size.expandedBy(dx: 0, dy: 1)

        var y = rect.origin.y
        var string = ""
        rect.byRow().forEach { point in
            if point.y > y {
                string += "\n"
                y = point.y
            }
            if points.contains(point) {
                string += "#"
            } else {
                string += "."
            }
        }

        return string
    }
}

private let regex = Regex("position=<(.+),(.+)> velocity=<(.+),(.+)>")

private extension Day10.MovingPoint {

    init(string: String) {
        let match = regex.matchOne(in: string)!
        self.init(point: .init(x: match.capturedValue(at: 1)!,
                               y: match.capturedValue(at: 2)!),
                  velocity: .init(dx: match.capturedValue(at: 3)!,
                                  dy: match.capturedValue(at: 4)!))
    }
}
