//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

private func solve(day: Int) throws {
    let input = try Input(file: File(index: day))
    let s = solver(at: day)
    print("Solutions for day \(day)")
    print("A: \(try s.solveFirst(input: input))")
    print("B: \(try s.solveSecond(input: input))")
}

private func solver(at index: Int) -> Solver {
    switch index {
    case 1: return Day1()
    case 2: return Day2()
    case 3: return Day3()
    case 4: return Day4()
    case 5: return Day5()
//    case 6: return Day6()
//    case 7: return Day7()
//    case 8: return Day8()
//    case 9: return Day9()
    default: fatalError()
    }
}

do {
    try solve(day: 5)
} catch let error {
    print(error)
}
