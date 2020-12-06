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

    init(string: String) {
        self.data = Data(string.utf8)
    }

    init(file: File) throws {
        self.data = try Data(contentsOf: file.url)
    }

    func string() -> String {
        return String(data: data, encoding: .utf8)!.trimmingCharacters(in: .newlines)
    }

    func lines(includingEmpty: Bool = false) -> [String] {
        return string().split(separator: "\n", omittingEmptySubsequences: !includingEmpty).map(String.init)
    }

    func int() -> Int {
        return Int(string())!
    }
}

protocol Solver {

    var mockInput: String? { get }

    func solveFirst(input: Input) throws -> String
    func solveSecond(input: Input) throws -> String
}

extension Solver {

    var mockInput: String? {
        return nil
    }


    func solveFirst(input: Input) throws -> String {
        return "NOT IMPLEMENTED"
    }

    func solveSecond(input: Input) throws -> String {
        return "NOT IMPLEMENTED"
    }
}
