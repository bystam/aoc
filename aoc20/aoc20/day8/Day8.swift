//
//  Day8.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-08.
//

import Foundation

struct Day8: Day {

    struct Machine {
        var program: [Instruction]
        var pc: Int = 0
        var acc: Int = 0

        init(input: Input) {
            program = input.lines().enumerated().map { line, string in
                let comps = string.components(separatedBy: " ")
                return Instruction(
                    line: line, operation: Operation(rawValue: comps[0])!, value: Int(comps[1])!
                )
            }
        }

        mutating func run() -> Exit {
            var linesSeen: Set<Int> = []
            while pc < program.endIndex {
                if linesSeen.contains(pc) {
                    return .infiniteLoop
                }

                linesSeen.insert(pc)
                let instruction = program[pc]
                switch instruction.operation {
                case .acc:
                    acc += instruction.value
                    pc += 1
                case .jmp:
                    pc += instruction.value
                case .nop:
                    pc += 1
                }
            }
            return .completed
        }

        mutating func reset() {
            pc = 0
            acc = 0
        }
    }

    struct Instruction {
        var line: Int
        var operation: Operation
        var value: Int
    }

    enum Operation: String {
        case acc, jmp, nop

        mutating func flip() {
            switch self {
            case .jmp: self = .nop
            case .nop: self = .jmp
            default: break
            }
        }
    }

    enum Exit {
        case infiniteLoop, completed
    }

    static func first() -> String {
        var machine = Machine(input: day8_input)
        let exit = machine.run()
        assert(exit == .infiniteLoop)
        return machine.acc.description
    }

    static func second() -> String {
        var machine = Machine(input: day8_input)
        for index in machine.program.indices {
            machine.program[index].operation.flip()
            let exit = machine.run()
            if exit == .completed {
                return machine.acc.description
            }
            machine.program[index].operation.flip()
            machine.reset()
        }
        fatalError()
    }
}
