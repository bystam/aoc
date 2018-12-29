//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

final class Day11: Solver {

    private typealias Level = Int

    struct Square: Hashable {
        let origin: Point
        let size: Int

        func shrinked() -> Square {
            return Square(origin: origin, size: size - 1)
        }

        func rightBottomSidePoints() -> [Point] {
            let rightSideX = origin.x + size - 1
            let bottomSideY = origin.y + size - 1

            var points = (origin.y...bottomSideY)
                .map { Point(x: rightSideX, y: $0) }

            points += (origin.x..<rightSideX)
                .map { Point(x: $0, y: bottomSideY) }
                .reversed()

            return points
        }
    }

    private var serialNumber: Int = -1
    private var pointLevels: [Point: Level] = [:]

    func solveFirst(input: Input) throws -> String {
        serialNumber = Int(input.string().trimmingCharacters(in: .whitespacesAndNewlines))!

        let maxSquare = calculateMaxSquare(ofSize: 3)
        return "\(maxSquare.square.origin.x),\(maxSquare.square.origin.y)"
    }

    func solveSecond(input: Input) throws -> String {
        serialNumber = Int(input.string().trimmingCharacters(in: .whitespacesAndNewlines))!

        var dynprogResults: [Square: Level] = [:]
        let maxSquare = (1...300)
            .map { size -> (square: Square, level: Int) in
                dynprogMaxSquare(ofSize: size, results: &dynprogResults)
            }
            .max(by: { $0.level < $1.level })!
        return "\(maxSquare.square.origin.x),\(maxSquare.square.origin.y),\(maxSquare.square.size)"
    }

    private func calculateMaxSquare(ofSize size: Int) -> (square: Square, level: Int) {
        return Rect(x: 1, y: 1, width: 300 - (size - 1), height: 300 - (size - 1))
            .byColumn()
            .map { point -> (square: Square, level: Int) in
                let square = Square(origin: point, size: size)
                return (square, calculateSquareLevel(startingAt: point, size: size))
            }
            .max(by: { $0.level < $1.level })!
    }

    private func calculateSquareLevel(startingAt point: Point, size: Int) -> Int {
        return Rect(origin: point, size: Size(width: size, height: size))
            .byColumn()
            .reduce(into: 0, { $0 += level(of: $1) })
    }

    private func dynprogMaxSquare(ofSize size: Int, results: inout [Square: Level]) -> (square: Square, level: Int) {
        return Rect(x: 1, y: 1, width: 300 - (size - 1), height: 300 - (size - 1))
            .byColumn()
            .map { point -> (square: Square, level: Int) in
                let square = Square(origin: point, size: size)
                return (square, dynprogLevel(of: square, results: &results))
            }
            .max(by: { $0.level < $1.level })!
    }

    private func dynprogLevel(of square: Square, results: inout [Square: Level]) -> Level {
        if square.size == 1 {
            let level = self.level(of: square.origin)
            results[square] = level
            return level
        }

        let level = square.rightBottomSidePoints()
            .reduce(results[square.shrinked()]!, { level, point in
                return level + self.level(of: point)
            })
        results[square] = level
        return level
    }

    private func level(of point: Point) -> Level {
        if let level = pointLevels[point] {
            return level
        }
        let rackID = point.x + 10
        var level = rackID * point.y
        level += serialNumber
        level *= rackID
        level = (level / 100) % 10
        level -= 5
        pointLevels[point] = level
        return level
    }
}
