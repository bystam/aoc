//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

final class Day6: Solver {

    private typealias Coordinate = Point
    private typealias Location = Point

    func solveFirst(input: Input) throws -> String {
        let points = Set(input.lines().map(Point.init))
        let rect = Algorithms
            .boundingRect(of: points)
            .insetBy(dx: -2, dy: -2)

        let coordinateClosestToLocation = rect.byColumn()
            .reduce(into: [Location: Coordinate](), { acc, location in
                acc[location] = closestCoordinate(to: location, from: points)
            })
        let coordinateAreaSizes = coordinateClosestToLocation
            .values
            .countedOccurrences()

        let infiniteAreaCoordinates = Set(rect.border()
            .map { borderPoint in coordinateClosestToLocation[borderPoint]! })

        return points
            .subtracting(infiniteAreaCoordinates)
            .map { coordinateAreaSizes[$0] ?? 0 }
            .max()!
            .description
    }

    func solveSecond(input: Input) throws -> String {
        let points = Set(input.lines().map(Point.init))
        let rect = Algorithms
            .boundingRect(of: points)
            .insetBy(dx: -2, dy: -2)

        let locationDistancesToAllCoordinates = rect.byColumn()
            .reduce(into: [Location: Int](), { acc, location in
                acc[location] = totalDistance(from: location, toAll: points)
            })

        return locationDistancesToAllCoordinates
            .filter { $0.value < 10000 }
            .count
            .description
    }

    private func closestCoordinate(to location: Location, from candidates: Set<Coordinate>) -> Coordinate {
        return candidates.min(by: { $0.manhattanDistance(to: location) < $1.manhattanDistance(to: location) })!
    }

    private func totalDistance(from location: Location, toAll coordinates: Set<Coordinate>) -> Int {
        return coordinates.reduce(into: 0, { acc, coordinate in
            acc += coordinate.manhattanDistance(to: location)
        })
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
