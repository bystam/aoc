//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

struct File {
    let url: URL

    init(index: Int) {
        let home = FileManager.default.homeDirectoryForCurrentUser
        self.url = home.appendingPathComponent("/workspace/AOC/days/day\(index)")
    }
}

struct Input {

    private let data: Data

    init(file: File) throws {
        self.data = try Data(contentsOf: file.url)
    }

    func string() -> String {
        return String(data: data, encoding: .utf8)!
    }

    func lines() -> [String] {
        return string().split(separator: "\n").map(String.init)
    }
}

protocol Solver {
    func solveFirst(input: Input) throws -> String
    func solveSecond(input: Input) throws -> String
}
