//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

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
    default: fatalError()
    }
}

do {
    try solve(day: 3)
} catch let error {
    print(error)
}
