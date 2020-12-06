//
//  AOCTests.swift
//  AOCTests
//
//  Created by Fredrik Bystam on 2018-12-21.
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import XCTest
@testable import AOC

class AOCTests: XCTestCase {

    func testLcs() {
        let lcs = "hej".lcs(with: "hejsan")
        XCTAssertEqual("hej", String(lcs))
    }

}
