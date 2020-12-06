//
//  Copyright © 2019 Fredrik Bystam. All rights reserved.
//

import Foundation

private enum UnitAction {
    case move(point: Point)
    case attack(index: Int)
}

private enum Direction: CaseIterable {
    case up, left, right, down // reading order
}

private enum MapPart {
    case open, wall
}

private enum Allegiance {
    case elf, goblin
}

private struct Unit {
    let allegiance: Allegiance
    let attackPower: Int
    var health: Int
    var position: Point
}

private struct Map {
    let matrix: Matrix<MapPart>
}


final class Day15: Solver {

//    var mockInput: String? {
//        return """
//        #########
//        #G......#
//        #.E.#...#
//        #..##..G#
//        #...##..#
//        #...#...#
//        #.G...G.#
//        #.....G.#
//        #########
//        """
//    }

    private var map: Map = Map(matrix: Matrix(width: 1, height: 1, initialValue: .open))
    private var units: [Unit] = [] {
        didSet {
            cachedUnitPositions.removeAll()
        }
    }

    private var cachedUnitPositions: [Point: Unit] = [:]
    private var unitPositions: [Point: Unit] {
        if cachedUnitPositions.isEmpty {
            cachedUnitPositions = units.indexed(by: ^\.position)
        }
        return cachedUnitPositions
    }

    func solveFirst(input: Input) throws -> String {
        (self.map, self.units) = parseInput(input.lines())

        print("\n==== Initial state ====\n")
        print(stringify(map: map, units: units))

        var r = 0
        outerloop: while r < 100000 {

            units.sort(by: readingOrder)

            var i = 0
            while i < units.count {

                let isDone = Set(units.map(^\.allegiance)).count == 1
                if isDone {
                    break outerloop
                }

                if let step = bfsToClosestAvailableEnemy(from: units[i]).first {
                    units[i].position = step
                }

                if let targetIndex = findAttackableSurroundingTarget(of: units[i]) {
                    units[targetIndex].health -= units[i].attackPower

                    if units[targetIndex].isDead {
                        units.remove(at: targetIndex)
                        if targetIndex < i {
                            i -= 1
                        }
                    }
                }

                i += 1
            }

            print("\n==== State after \(r+1) round(s) ====\n\(stringify(map: map, units: units))")

            r += 1
        }

        let totalHealth = units.reduce(into: 0) { acc, unit in
            acc += unit.health
        }

        return (totalHealth * r).description
    }

    private func findAttackableSurroundingTarget(of unit: Unit) -> Int? {
        let surroundings = allOpenPositions(around: unit.position, includingUnits: false)
        return units.enumerated()
            .filter { $0.element.isEnemy(of: unit) && surroundings.contains($0.element.position) }
            .min(by: {
                let unit1 = $0.element
                let unit2 = $1.element
                if unit1.health < unit2.health { return true }
                if unit1.health > unit2.health { return false }
                return readingOrder(unit1, unit2)
            })?
            .offset
    }

    private func bfsToClosestAvailableEnemy(from unit: Unit) -> [Point] {
        let isAlreadyNextToEnemy = allOpenPositions(around: unit.position, includingUnits: false)
            .contains(where: { unitPositions[$0]?.isEnemy(of: unit) == true })
        if isAlreadyNextToEnemy {
            return []
        }

        var searchSpace: [Point: Point] = [:]
        let possibleGoals = Set(units
            .filter { $0.isEnemy(of: unit) }
            .flatMap { allOpenPositions(around: $0.position, includingUnits: true) }
        )
        var goalPoint: Point? = nil

        var queue: [Point] = [unit.position]
        while !queue.isEmpty {
            let point = queue.removeFirst()
            if possibleGoals.contains(point) {
                goalPoint = point
                break
            }

            let surroundings = allOpenPositions(around: point, includingUnits: true)
                .filter { searchSpace[$0] == nil }
            surroundings.forEach { searchSpace[$0] = point }
            queue += surroundings
        }

        guard let goal = goalPoint, goal != unit.position else {
            return []
        }

        var path: [Point] = [goal]
        var p = goal
        while let next = searchSpace[p], next != unit.position {
            path.append(next)
            p = next
        }
        return path.reversed()
    }

    private func allOpenPositions(around position: Point, includingUnits: Bool) -> [Point] {
        return Direction.allCases.compactMap { direction -> Point? in
            let nextToPoint = position.applying(direction.vector)
            if map.matrix[nextToPoint] == .wall {
                return nil
            }
            if includingUnits && unitPositions.keys.contains(nextToPoint) {
                return nil
            }
            return nextToPoint
        }
    }

    private func readingOrder(_ l: Unit, _ r: Unit) -> Bool {
        return readingOrder(l.position, r.position)
    }

    private func readingOrder(_ l: Point, _ r: Point) -> Bool {
        if l.y < r.y { return true }
        if l.y > r.y { return false }
        return l.x < r.x
    }
}


extension Direction {

    var vector: Vector {
        switch self {
        case .up: return Vector(dx: 0, dy: -1)
        case .right: return Vector(dx: 1, dy: 0)
        case .down: return Vector(dx: 0, dy: 1)
        case .left: return Vector(dx: -1, dy: 0)
        }
    }
}

extension Unit {
    init(allegiance: Allegiance, position: Point) {
        self.init(allegiance: allegiance, attackPower: 3, health: 200, position: position)
    }

    var isDead: Bool {
        return health <= 0
    }

    func isEnemy(of other: Unit) -> Bool {
        return allegiance != other.allegiance
    }
}

extension Map {

}

private func parseInput(_ lines: [String]) -> (Map, [Unit]) {
    var matrix: Matrix<MapPart> = Matrix(width: lines[0].count, height: lines.count, initialValue: .wall)
    var units: [Unit] = []
    lines.enumerated().forEach { y, line in
        line.enumerated().forEach { x, char in
            let position = Point(x: x, y: y)
            switch char {
            case "G":
                units.append(Unit(allegiance: .goblin, position: position))
                matrix[position] = .open
            case "E":
                units.append(Unit(allegiance: .elf, position: position))
                matrix[position] = .open
            case ".":
                matrix[position] = .open
            case "#":
                matrix[position] = .wall
            default: fatalError()
            }
        }
    }
    return (Map(matrix: matrix), units)
}

private func stringify(map: Map, units: [Unit]) -> String {
    return map.matrix.stringify { point, part -> Character in
        let unit = units.first(where: { $0.position == point })
        switch (part, unit?.allegiance) {
        case (_, .goblin?): return "G"
        case (_, .elf?): return "E"
        case (.open, nil): return "."
        case (.wall, nil): return "#"
        }
    }
}
