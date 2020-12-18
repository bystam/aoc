//
//  Day18.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-18.
//

import Foundation

struct Day18: Day {

    struct Expression {
        var operands: [Operand] = []
        var operators: [Operator] = []
    }

    enum Operand {
        case leaf(Int)
        case expression(Expression)
    }

    enum Operator {
        case addition
        case multiplication
    }

    static func first() -> String {
        let expressions = day18_input.lines()
            .map { string -> Expression in
                var index = string.startIndex
                let expression = parse(string: string, at: &index)
                assert(index == string.endIndex)
                return expression
            }
        let results = expressions.map(evaluateSimple(_:))
        return results.sum().description
    }

    private static func evaluateSimple(_ expression: Expression) -> Int {
        var result = simpleValue(of: expression.operands[0])
        for (index, operand) in expression.operands.dropFirst().enumerated() {
            switch expression.operators[index] {
            case .addition: result += simpleValue(of: operand)
            case .multiplication: result *= simpleValue(of: operand)
            }
        }
        return result
    }

    private static func simpleValue(of operand: Operand) -> Int {
        switch operand {
        case .leaf(let int): return int
        case .expression(let expression): return evaluateSimple(expression)
        }
    }

    static func second() -> String {
        let expressions = day18_input.lines()
            .map { string -> Expression in
                var index = string.startIndex
                let expression = parse(string: string, at: &index)
                assert(index == string.endIndex)
                return expression
            }
        let results = expressions.map(evaluateAdvanced(_:))
        return results.sum().description
    }

    private static func evaluateAdvanced(_ expression: Expression) -> Int {
        var multiplicationIndices = expression.operators.indices
            .filter { expression.operators[$0] == .multiplication }

        multiplicationIndices.append(expression.operators.endIndex)

        var result = 1
        var start = expression.operands.startIndex
        for index in multiplicationIndices {
            let additionSegment = expression.operands[start...index]
            result *= additionSegment.map { advancedValue(of: $0) }.sum()
            start = index + 1
        }

        return result
    }

    private static func advancedValue(of operand: Operand) -> Int {
        switch operand {
        case .leaf(let int): return int
        case .expression(let expression): return evaluateAdvanced(expression)
        }
    }

    private static func parse(string: String, at index: inout String.Index) -> Expression {
        var expression = Expression()
        while index < string.endIndex {
            let char = string[index]
            switch char {
            case "0"..."9":
                let int = Int(char.asciiValue! - 48)
                expression.operands.append(.leaf(int))
                index = string.index(after: index)
            case "+":
                expression.operators.append(.addition)
                index = string.index(after: index)
            case "*":
                expression.operators.append(.multiplication)
                index = string.index(after: index)
            case "(":
                index = string.index(after: index)
                expression.operands.append(
                    .expression(parse(string: string, at: &index))
                )
            case ")":
                index = string.index(after: index)
                return expression
            default:
                assert(char == " ")
                index = string.index(after: index)
            }
        }
        return expression
    }
}
