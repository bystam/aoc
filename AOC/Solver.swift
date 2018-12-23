//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

protocol Solver {
    func solveFirst(input lines: [String]) throws -> String
    func solveSecond(input lines: [String]) throws -> String
}
