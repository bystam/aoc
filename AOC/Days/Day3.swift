//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

final class Day3: Solver {

    fileprivate struct Claim {
        let id: String
        let rect: Rect
    }

    func solveFirst(input: Input) throws -> String {
        let squares = input.lines()
            .map(Claim.init)
            .reduce(into: Matrix<Int>(width: 1000, height: 1000, initialValue: 0)) { fabric, claim in
                claim.rect.forEach { p in
                    fabric[x: p.x, y: p.y] += 1
                }
            }
            .filter { overlapCount in overlapCount >= 2 }
            .count

        return squares.description
    }

    func solveSecond(input: Input) throws -> String {
        let claims = input.lines().map(Claim.init)
        let fabric = claims
            .reduce(into: Matrix<Int>(width: 1000, height: 1000, initialValue: 0)) { fabric, claim in
                claim.rect.forEach { p in
                    fabric[x: p.x, y: p.y] += 1
                }
            }
        let single = claims.first(where: { claim in
            claim.rect.allSatisfy({ p in fabric[x: p.x, y: p.y] == 1 })
        })

        return single!.id
    }
}

private let regex = Regex("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)")

extension Day3.Claim {

    init(string: String) {
        let match = regex.matchOne(in: string)!
        let r = Rect(origin: .init(x: match.capturedValue(at: 2)!, y: match.capturedValue(at: 3)!),
                     size: .init(width: match.capturedValue(at: 4)!, height: match.capturedValue(at: 5)!))
        self.init(id: match.capturedValue(at: 1)!, rect: r)
    }
}
