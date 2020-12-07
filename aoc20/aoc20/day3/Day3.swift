//
//  Day3.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-07.
//

import Foundation

struct Day3: Day {

    internal struct Map {
        internal let grid: [[Character]]

        init(lines: [String]) {
            grid = lines.map { Array($0) }
        }

        func numberOfTrees(following slope: Vector) -> Int {
            var point = Point(x: 0, y: 0)

            var trees = 0
            while point.y < grid.count {
                if char(at: point) == "#" {
                    trees += 1
                }
                point = point.offset(by: slope)
            }
            return trees
        }

        private func char(at point: Point) -> Character {
            var real = point
            real.x = real.x % grid[0].count
            return grid[real.y][real.x]
        }

        private func isBeyondEdge(_ point: Point) -> Bool {
            point.y >= grid.count
        }
    }

    static func first() -> String {
        let map = Map(lines: day3_input.lines())
        let slope = Vector(dx: 3, dy: 1)
        return map.numberOfTrees(following: slope).description
    }

    static func second() -> String {
        let map = Map(lines: day3_input.lines())
        let slopes: [Vector] = [
            .init(dx: 1, dy: 1),
            .init(dx: 3, dy: 1),
            .init(dx: 5, dy: 1),
            .init(dx: 7, dy: 1),
            .init(dx: 1, dy: 2)
        ]
        let allTrees = slopes.map { map.numberOfTrees(following: $0) }
        return allTrees.reduce(1, *).description
    }
}
