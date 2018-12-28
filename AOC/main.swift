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
    Day10(),
]

private func solve(day: Int? = nil) throws {
    let day = day ?? kSolvers.endIndex
    let solver = kSolvers[day - 1]

    let input: Input
    if let mock = solver.mockInput {
        input = Input(string: mock)
    } else {
        input = try Input(file: File(index: day))
    }

    print("Solutions for day \(day)")
    print("A: \(try solver.solveFirst(input: input))")
    print("B: \(try solver.solveSecond(input: input))")
}

do {
    try solve(day: 8)
} catch let error {
    print(error)
}
