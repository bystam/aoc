//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

protocol Solver {
    func solveFirst(input lines: [String]) throws -> String
    func solveSecond(input lines: [String]) throws -> String
}

extension Solver {

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

struct File {
    let url: URL

    init(index: Int) {
        let home = FileManager.default.homeDirectoryForCurrentUser
        self.url = home.appendingPathComponent("/workspace/AOC/days/day\(index)")
    }
}
