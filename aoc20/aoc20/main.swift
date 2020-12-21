//
//  main.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-06.
//

import Foundation

private func measure<T>(_ task: () -> T) -> (T, time: String) {
    let start = DispatchTime.now().rawValue
    let value = task()
    let time = DispatchTime.now().rawValue - start
    let interval = TimeInterval(time) / 1_000_000_000.0
    return (value, String(format: "%.3f", interval))
}

func run<D: Day>(_ type: D.Type) {
    let title = "######## Running \(type) ########"
    print(title)

    let (firstResult, firstTime) = measure {
        type.first()
    }
    print("""

    First output (time: \(firstTime) s):
        \(firstResult)
    """)

    let (secondResult, secondTime) = measure {
        type.second()
    }
    print("""

    Second output (time: \(secondTime) s):
        \(secondResult)

    """)

    print(String(repeating: "#", count: title.count))
}

run(Day21.self)
