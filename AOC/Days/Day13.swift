//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

final class Day13: Solver {

    fileprivate struct Direction: Equatable {
        let vector: Vector

        static let north = Direction(Vector(dx: 0, dy: -1))
        static let east = Direction(Vector(dx: 1, dy: 0))
        static let south = Direction(Vector(dx: 0, dy: 1))
        static let west = Direction(Vector(dx: -1, dy: 0))

        private init(_ vector: Vector) {
            self.vector = vector
        }

        func turnedLeft() -> Direction {
            return Direction(vector.rotatedCounterclockwise())
        }

        func turnedRight() -> Direction {
            return Direction(vector.rotatedClockwise())
        }
    }

    fileprivate enum CartTurn {
        case left, fowards, right
    }

    fileprivate struct Cart {
        private(set) var point: Point
        private(set) var direction: Direction
        private(set) var nextTurn: CartTurn = .left

        init(point: Point, direction: Direction) {
            self.point = point
            self.direction = direction
        }

        mutating func move() {
            point = point.applying(direction.vector)
        }

        mutating func intersectionTurn() {
            switch nextTurn {
            case .left:
                direction = direction.turnedLeft()
                nextTurn = .fowards
            case .fowards:
                nextTurn = .right
            case .right:
                direction = direction.turnedRight()
                nextTurn = .left
            }
        }
    }

    /// relative to moving northbound
    fileprivate enum RoadTurn {
        case west, east
    }

    /// relative to moving northbound
    fileprivate enum Alignment {
        case horizontal, vertical
    }

    fileprivate enum RoadPiece {
        case straight(Alignment)
        case intersection
        case turn(RoadTurn)
        case empty
    }

    fileprivate struct Map {
        let matrix: Matrix<RoadPiece>
        var carts: [Cart]
    }

    func solveFirst(input: Input) throws -> String {
        let map = Map(lines: input.lines())
        return "\n\(map)"
    }

    func solveSecond(input: Input) throws -> String {
        return ""
    }
}

extension Day13.Map {

    init(lines: [String]) {
        var matrix = Matrix<Day13.RoadPiece>(width: lines[0].count, height: lines.count, initialValue: .empty)
        var carts: [Day13.Cart] = []

        for y in (0..<lines.count) {
            for (x, char) in lines[y].enumerated() {
                let point = Point(x: x, y: y)
                let (piece, cart) = Day13.Map.parseRoadPiece(char, point: point)

                matrix[x: x, y: y] = piece
                if let cart = cart {
                    carts.append(cart)
                }
            }
        }

        self.init(matrix: matrix, carts: carts)
    }

    private static func parseRoadPiece(_ raw: Character, point: Point) -> (Day13.RoadPiece, Day13.Cart?) {
        switch raw {
        case "-":
            return (.straight(.horizontal), nil)
        case "|":
            return (.straight(.vertical), nil)
        case "+":
            return (.intersection, nil)
        case "/":
            return (.turn(.east), nil)
        case "\\":
            return (.turn(.west), nil)
        case "^":
            return (.straight(.vertical), Day13.Cart(point: point, direction: .north))
        case "<":
            return (.straight(.horizontal), Day13.Cart(point: point, direction: .west))
        case ">":
            return (.straight(.horizontal), Day13.Cart(point: point, direction: .east))
        case "v":
            return (.straight(.vertical), Day13.Cart(point: point, direction: .south))
        case " ":
            return (.empty, nil)
        default:
            fatalError()
        }
    }
}

extension Day13.Map: CustomStringConvertible {

    var description: String {
        let carts = self.carts.indexed(by: ^\.point)

        return matrix.stringify { point, piece in
            if let cart = carts[point] {
                if cart.direction == .north { return "^" }
                if cart.direction == .east { return ">" }
                if cart.direction == .south { return "v" }
                if cart.direction == .west { return "<" }
                fatalError()
            } else {
                switch matrix[point] {
                case .straight(.horizontal): return "-"
                case .straight(.vertical): return "|"
                case .intersection: return "+"
                case .turn(.west): return "\\"
                case .turn(.east): return "/"
                case .empty: return " "
                }
            }
        }
    }
}
