//
//  main.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-06.
//

import Foundation

func run<D: Day>(_ type: D.Type) {
    print("""
    #### Running \(type) ####

    First output:
        \(type.first())

    Second output:
        \(type.second())

    #########################
    """)
}

run(Day9.self)
