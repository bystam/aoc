//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

private extension Solver {

    func solveFirst(file: File) throws -> String {
        let data = try Data(contentsOf: file.url)
        let lines = String(data: data, encoding: .utf8)!.split(separator: "\n").map(String.init)
        return try solveFirst(input: lines)
    }

    func solveSecond(file: File) throws -> String {
        let data = try Data(contentsOf: file.url)
        let lines = String(data: data, encoding: .utf8)!.split(separator: "\n").map(String.init)
        return try solveSecond(input: lines)
    }
}

private struct File {
    let url: URL

    init(index: Int) {
        let home = FileManager.default.homeDirectoryForCurrentUser
        self.url = home.appendingPathComponent("/workspace/AOC/days/day\(index)")
    }
}

private func solve(day: Int) throws {
    let file = File(index: day)
    let s = solver(at: day)
    print("Solutions for day \(day)")
    print("A: \(try s.solveFirst(file: file))")
    print("B: \(try s.solveSecond(file: file))")
}

private func solver(at index: Int) -> Solver {
    switch index {
    case 1: return Day1()
    case 2: return Day2()
    case 3: return Day3()
    case 4: return Day4()
//    case 5: return Day5()
//    case 6: return Day6()
//    case 7: return Day7()
//    case 8: return Day8()
//    case 9: return Day9()
    default: fatalError()
    }
}

do {
    try solve(day: 4)
} catch let error {
    print(error)
}
