//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

final class Day3: Solver {

    fileprivate struct Claim {
        let id: String
        let rect: Rect
    }

    func solveFirst(input lines: [String]) throws -> String {
        let fabric = Matrix<Int>(width: 1001, height: 1001, initialValue: 0)
        let squares = lines
            .map(Claim.init)
            .reduce(into: fabric) { fabric, claim in
                claim.rect.forEach { p in
                    fabric[x: p.x, y: p.y] += 1
                }
            }
            .filter { overlapCount in overlapCount >= 2 }
            .count

        return squares.description
    }

    func solveSecond(input lines: [String]) throws -> String {
        return ""
    }
}

private let regex = DumbassRegex("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)")

extension Day3.Claim {

    init(string: String) {
        let match = regex.matchOne(in: string)!
        let r = Rect(origin: .init(x: match.capturedValue(at: 2)!, y: match.capturedValue(at: 3)!),
                     size: .init(width: match.capturedValue(at: 4)!, height: match.capturedValue(at: 5)!))
        self.init(id: match.capturedValue(at: 1)!, rect: r)
    }
}
