//
//  Day17.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-17.
//

import Foundation

fileprivate protocol Day17Point: Hashable {
    func neighbors() -> [Self]
}

struct Day17: Day {

    struct Point3: Day17Point {
        var x, y, z: Int

        func neighbors() -> [Point3] {
            cross((x-1...x+1), (y-1...y+1), (z-1...z+1))
                .map { Point3(x: $0, y: $1, z: $2) }
                .filter { $0 != self }
        }
    }

    struct Point4: Day17Point {
        var x, y, z, w: Int

        func neighbors() -> [Point4] {
            cross((x-1...x+1), (y-1...y+1), (z-1...z+1), (w-1...w+1))
                .map { Point4(x: $0, y: $1, z: $2, w: $3) }
                .filter { $0 != self }
        }
    }

    struct PointState {
        var active: Bool = false
        var activeNeighbors: Int = 0
    }

    static func first() -> String {
        var currentlyActive: Set<Point3> = []
        day17_input.lines().enumerated().forEach { y, line in
            line.enumerated().forEach { x, char in
                if char == "#" { currentlyActive.insert(.init(x: x, y: y, z: 0)) }
            }
        }

        return boot(with: currentlyActive).count.description
    }

    static func second() -> String {
        var currentlyActive: Set<Point4> = []
        day17_input.lines().enumerated().forEach { y, line in
            line.enumerated().forEach { x, char in
                if char == "#" { currentlyActive.insert(.init(x: x, y: y, z: 0, w: 0)) }
            }
        }

        return boot(with: currentlyActive).count.description
    }

    private static func boot<P: Day17Point>(with starting: Set<P>) -> Set<P> {
        var currentlyActive: Set<P> = starting

        (0..<6).forEach { _ in
            var currentState: [P: PointState] = [:]
            currentlyActive.forEach { point in
                currentState[point] = .init(active: true)
            }
            currentlyActive.forEach { active in
                active.neighbors().forEach { point in
                    currentState[point, default: PointState()].activeNeighbors += 1
                }
            }

            currentState.forEach { point, state in
                switch (state.active, state.activeNeighbors) {
                case (true, (2...3)): // remain active
                    break
                case (true, _): // become inactive
                    currentlyActive.remove(point)
                case (false, 3): // become active
                    currentlyActive.insert(point)
                case (false, _): // remain inactive
                    break
                }
            }
        }

        return currentlyActive
    }
}
