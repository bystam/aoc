//
//  Algorithm.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-09.
//

import Foundation

enum BinarySearchResult {
    case equal, below, above
}

extension RandomAccessCollection where Index == Int {

    func binarySearch(with compare: (Element) -> BinarySearchResult) -> Index? {
        var lower = startIndex
        var upper = endIndex - 1
        while lower <= upper {
            let current = Int(floor(Double(lower + upper) / 2.0))
            let value = self[current]
            switch compare(value) {
            case .equal:
                return current
            case .below:
                upper = current - 1
            case .above:
                lower = current + 1
            }
        }
        return nil
    }
}

extension RandomAccessCollection where Element: Comparable, Index == Int {

    func binarySearch(for target: Element) -> Index? {
        return binarySearch { element -> BinarySearchResult in
            if target == element { return .equal }
            if target < element { return .below }
            if target > element { return .above }
            fatalError()
        }
    }
}

extension Sequence where Element: Numeric {
    func sum() -> Element {
        reduce(0, +)
    }
}
