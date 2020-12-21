//
//  Day20.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-20.
//

import Foundation

struct Day20: Day {

    enum Direction {
        case top, bottom, left, right
    }

    struct Connection: Hashable {
        var pattern: Pattern
        var direction: Direction
    }

    struct Puzzle {
        var size: Int
        var tiles: [Tile]
        var lookup: [Connection: Set<PlacedTile>] = [:]

        var end: Point { .init(x: size - 1, y: size - 1) }
    }

    struct State {
        var size: Int
        var grid: [[PlacedTile?]]
        var used: Set<Int> = []

        init(size: Int) {
            self.size = size
            self.grid = .init(repeating: .init(repeating: nil, count: size), count: size)
        }

        subscript(point: Point) -> PlacedTile? {
            get {
                if point.x < 0 || point.x >= size { return nil }
                if point.y < 0 || point.y >= size { return nil }
                return grid[point.y][point.x]
            }
            set {
                grid[point.y][point.x] = newValue
                if let value = newValue {
                    used.insert(value.id)
                } else {
                    used = Set(grid.flatMap { $0 }.compactMap { $0?.id })
                }
            }
        }
    }

    struct Tile {
        var id: Int
        var permutations: [Permutation]
    }

    struct PlacedTile: Hashable {
        var id: Int
        var permutation: Permutation
    }

    struct Permutation: Hashable {
        var pixels: [[Character]]
        var top: Pattern = .init()
        var bottom: Pattern = .init()
        var right: Pattern = .init()
        var left: Pattern = .init()
    }

    struct Pattern: Hashable {
        var int: UInt16 = 0
    }

    static func first() -> String {
        let puzzle = buildPuzzle()
        let solution = solve(puzzle)
        var result = 1
        result *= solution[.init(x: 0, y: 0)]!.id
        result *= solution[.init(x: puzzle.size - 1, y: 0)]!.id
        result *= solution[.init(x: 0, y: puzzle.size - 1)]!.id
        result *= solution[.init(x: puzzle.size - 1, y: puzzle.size - 1)]!.id
        return result.description
    }

    static func second() -> String {
        let puzzle = buildPuzzle()
        let solution = solve(puzzle)
        let picture = createPicture(from: solution)
        let roughnesses: [Int] = [
            picture,
            rotate(picture),
            rotate(rotate(picture)),
            rotate(rotate(rotate(picture))),
            flip(picture),
            rotate(flip(picture)),
            rotate(rotate(flip(picture))),
            rotate(rotate(rotate(flip(picture))))
        ].map { calculateRoughness(in: $0) }
        return roughnesses.max()!.description
    }

    private static func buildPuzzle() -> Puzzle {
        let tiles = day20_input.lines(separator: "\n\n").map(Tile.init(string:))
        var puzzle = Puzzle(size: Int(sqrt(Double(tiles.count))), tiles: tiles)
        for tile in tiles {
            for permutation in tile.permutations {
                puzzle.lookup[.init(pattern: permutation.top, direction: .top), default: []].insert(
                    .init(id: tile.id, permutation: permutation)
                )
                puzzle.lookup[.init(pattern: permutation.bottom, direction: .bottom), default: []].insert(
                    .init(id: tile.id, permutation: permutation)
                )
                puzzle.lookup[.init(pattern: permutation.left, direction: .left), default: []].insert(
                    .init(id: tile.id, permutation: permutation)
                )
                puzzle.lookup[.init(pattern: permutation.right, direction: .right), default: []].insert(
                    .init(id: tile.id, permutation: permutation)
                )
            }
        }
        return puzzle
    }

    private static func solve(_ puzzle: Puzzle) -> State {
        for tile in puzzle.tiles {
            for permutation in tile.permutations {
                var start = State(size: puzzle.size)
                start[.origin] = .init(id: tile.id, permutation: permutation)
                let (success, result) = solve(puzzle, at: .init(x: 1, y: 0), state: start)
                if success {
                    return result
                }
            }

        }
        fatalError()
    }

    private static func solve(
        _ puzzle: Puzzle, at point: Point, state: State
    ) -> (Bool, State) {

        let connectionLeft = state[point.offset(x: -1)]?.permutation.right
        let connectionTop = state[point.offset(y: -1)]?.permutation.bottom
        let leftPossibilities = connectionLeft.flatMap { left in
            puzzle.lookup[.init(pattern: left.flipped(), direction: .left)]
        }
        let topPossibilities = connectionTop.flatMap { top in
            puzzle.lookup[.init(pattern: top.flipped(), direction: .top)]
        }

        var allPossibilities: Set<PlacedTile>
        switch (leftPossibilities, topPossibilities) {
        case let (left?, top?):
            allPossibilities = left.intersection(top)
        case let (left?, nil):
            allPossibilities = left
        case let (nil, top?):
            allPossibilities = top
        case (nil, nil):
            allPossibilities = []
        }
        allPossibilities = allPossibilities.filter { !state.used.contains($0.id) }

        if allPossibilities.isEmpty {
            return (false, state)
        }

        for tile in allPossibilities {
            var nextState = state
            nextState[point] = tile

            if point == puzzle.end {
                return (true, nextState)
            }

            var nextPoint = point.offset(x: 1)
            if nextPoint.x >= puzzle.size {
                nextPoint.x = 0
                nextPoint.y += 1
            }

            let (solved, output) = solve(puzzle, at: nextPoint, state: nextState)
            if solved {
                return (true, output)
            }
        }
        return (false, state)
    }

    private static func createPicture(from state: State) -> [[Character]] {
        var result: [[Character]] = []
        for tileRow in state.grid {
            let innerPixels = tileRow[0]!.permutation.pixels.indices
            for pixelRowIndex in innerPixels.dropFirst().dropLast() {
                var pixelRow: [Character] = []
                for tile in tileRow {
                    let pixels = tile!.permutation.pixels[pixelRowIndex].dropFirst().dropLast()
                    pixelRow.append(contentsOf: pixels)
                }
                result.append(pixelRow)
            }
        }
        return result
    }

    private static func stringify(_ state: State) -> String {
        var result = ""
        for row in state.grid {
            for i in (0..<row[0]!.permutation.pixels.count) {
                for tile in row {
                    result += String(tile!.permutation.pixels[i])
                    result += " "
                }
                result += "\n"
            }
            result += "\n"
        }
        return result
    }

    private static func calculateRoughness(in picture: [[Character]]) -> Int {
        let (monsters, monsterHits) = findSeaMonsters(in: picture)
        if monsters == 0 { return 0 }

        var roughness = 0
        for row in picture.indices {
            for col in picture[0].indices {
                if picture[row][col] == "#" && monsterHits[row][col] == false {
                    roughness += 1
                }
            }
        }

        return roughness
    }

    private static func findSeaMonsters(in picture: [[Character]]) -> (Int, [[Bool]]) {
        var seaMonsterHits: [[Bool]] = .init(
            repeating: .init(repeating: false, count: picture[0].count), count: picture.count
        )
        let seaMonster: [[Character]] = """
                          #
        #    ##    ##    ###
         #  #  #  #  #  #
        """.components(separatedBy: "\n").map { Array($0) }
        let seaMonsterHeight = seaMonster.count
        let seaMonsterWidth = seaMonster.map { $0.count }.max()!
        let seaMonsterIndices = cross(0..<seaMonsterHeight, 0..<seaMonsterWidth)
            .filter { y, x in seaMonster[safe: y]?[safe: x] == "#" }
        var total = 0
        for row in picture.indices.dropLast(seaMonsterHeight) {
            for col in picture[0].indices.dropLast(seaMonsterWidth) {
                var found = true
                for (y, x) in seaMonsterIndices {
                    // No sea monster here
                    if picture[y+row][x+col] != "#" {
                        found = false
                        break
                    }
                }
                if found {
                    total += 1
                    for (y, x) in seaMonsterIndices {
                        seaMonsterHits[y+row][x+col] = true
                    }
                }
            }
        }

        return (total, seaMonsterHits)
    }
}

extension Day20.Tile: PatternConvertible {
    static let pattern: String = "Tile (\\d+):\n([\\n.#]*)"

    init(match: PatternMatch) {
        let pixels = match.string(at: 1).components(separatedBy: "\n").map(Array.init)
        let permutation = Day20.Permutation(pixels: pixels)
        self.init(
            id: match.int(at: 0),
            permutations: [
                permutation,
                permutation.rotated(),
                permutation.rotated().rotated(),
                permutation.rotated().rotated().rotated(),
                permutation.flipped(),
                permutation.flipped().rotated(),
                permutation.flipped().rotated().rotated(),
                permutation.flipped().rotated().rotated().rotated(),
            ]
        )
    }
}

extension Day20.Permutation: CustomStringConvertible {

    var description: String {
        pixels.map { String($0) }.joined(separator: "\n")
    }

    init(pixels: [[Character]]) {
        self.pixels = pixels
        (0..<10).forEach { index in
            top[bit: index] = (pixels[0][index] == "#")
            bottom[bit: index] = (pixels[9][9 - index] == "#")
            right[bit: index] = (pixels[index][9] == "#")
            left[bit: index] = (pixels[9 - index][0] == "#")
        }
    }

    func rotated() -> Day20.Permutation {
        .init(pixels: rotate(pixels), top: left, bottom: right, right: top, left: bottom)
    }

    func flipped() -> Day20.Permutation {
        .init(pixels: flip(pixels),
              top: top.flipped(), bottom: bottom.flipped(),
              right: left.flipped(), left: right.flipped())
    }
}

extension Day20.Pattern: CustomStringConvertible {

    var description: String {
        (0..<10).map { self[bit: $0] ? "#" : "." }.joined()
    }

    subscript(bit offset: Int) -> Bool {
        get { int & (1 << offset) != 0 }
        set {
            if newValue {
                int |= 1 << offset
            } else {
                int &= ~(1 << offset)
            }
        }
    }

    func flipped() -> Day20.Pattern {
        var result: Day20.Pattern = .init()
        (0..<10).forEach { offset in
            result[bit: offset] = self[bit: 9 - offset]
        }
        return result
    }
}

private func rotate(_ pixels: [[Character]]) -> [[Character]] {
    var rotated = pixels
    for (row, col) in cross(rotated.indices, rotated[0].indices) {
        rotated[col][pixels.count - row - 1] = pixels[row][col]
    }
    return rotated
}

private func flip(_ pixels: [[Character]]) -> [[Character]] {
    var flipped = pixels
    flipped.indices.forEach { row in
        flipped[row].reverse()
    }
    return flipped
}
