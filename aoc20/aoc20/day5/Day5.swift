//
//  Day5.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-07.
//

import Foundation

struct Day5: Day {

    struct SeatMap {

        static let actualSeats = Grid(
            min: .init(x: 0, y: 1),
            max: .init(x: 7, y: 126)
        )

        var seats: [[Status]] = .init(
            repeating: .init(repeating: .vacant, count: 8),
            count: 128
        )

        mutating func occupy(following sequence: String) {
            var min = Point(x: 0, y: 0)
            var max = Point(x: 7, y: 127)
            sequence.forEach { char in
                if char == "B" { min.y = (min.y + max.y) / 2 + 1 }
                if char == "F" { max.y = (min.y + max.y) / 2 }
                if char == "R" { min.x = (min.x + max.x) / 2 + 1 }
                if char == "L" { max.x = (min.x + max.x) / 2 }
            }
            assert(min == max)
            seats[min.y][min.x] = .occupied
        }

        func ids(for status: Status) -> [Int] {
            Self.actualSeats
                .filter { p in seats[p.y][p.x] == status }
                .map { p in p.y * 8 + p.x }
        }
    }

    enum Status {
        case vacant, occupied
    }

    static func first() -> String {
        var map = SeatMap()
        day5_input.lines().forEach { seq in
            map.occupy(following: seq)
        }

        return map.ids(for: .occupied).max()!.description
    }

    static func second() -> String {
        var map = SeatMap()
        day5_input.lines().forEach { seq in
            map.occupy(following: seq)
        }
        let occupied = Set(map.ids(for: .occupied))
        let ids = map.ids(for: .vacant).filter { id -> Bool in
            occupied.contains(id - 1) && occupied.contains(id + 1)
        }

        return ids[0].description
    }
}
