//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

private let kSolvers: [Solver] = [
    Day1(),
    Day2(),
    Day3(),
    Day4(),
    Day5(),
    Day6(),
    Day7(),
    Day8(),
    Day9(),
]

private func solve(day: Int? = nil) throws {
    let day = day ?? kSolvers.endIndex
    let input = try Input(file: File(index: day))
    let s = kSolvers[day - 1]
    print("Solutions for day \(day)")
    print("A: \(try s.solveFirst(input: input))")
    print("B: \(try s.solveSecond(input: input))")
}

do {
    try solve(day: 7)
} catch let error {
    print(error)
}
