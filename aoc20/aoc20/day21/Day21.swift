//
//  Day21.swift
//  aoc20
//
//  Created by Fredrik Bystam on 2020-12-21.
//

import Foundation

struct Day21: Day {

    struct Food {
        var ingredients: Set<Ingredient> = []
        var allergens: Set<Allergen> = []
    }

    struct Ingredient: StringType {
        var string: String
    }

    struct Allergen: StringType {
        var string: String
    }

    static func first() -> String {
        let foods = day21_input.lines(Food.self)
        var deduction: [Allergen: Set<Ingredient>] = [:]
        for food in foods {
            for allergen in food.allergens {
                deduction[allergen, default: food.ingredients].formIntersection(food.ingredients)
            }
        }

        var nonAllergenic: Set<Ingredient> = Set(foods.flatMap { $0.ingredients })
        for allergenicIngredients in deduction.values {
            nonAllergenic.subtract(allergenicIngredients)
        }

        let totalNonallergenicOccurrances = foods.reduce(0) { acc, food -> Int in
            acc + food.ingredients.intersection(nonAllergenic).count
        }

        return totalNonallergenicOccurrances.description
    }

    static func second() -> String {
        let foods = day21_input.lines(Food.self)
        var deduction: [Allergen: Set<Ingredient>] = [:]
        for food in foods {
            for allergen in food.allergens {
                deduction[allergen, default: food.ingredients].formIntersection(food.ingredients)
            }
        }

        var result: [(allergen: Allergen, ingredient: Ingredient)] = []
        while deduction.count > 0 {
            let match = deduction.first(where: { allergen, ingredients -> Bool in
                ingredients.count == 1
            })!
            let (allergen, ingredient) = (match.key, match.value.first!)
            result.append((allergen, ingredient))
            deduction.removeValue(forKey: allergen)
            for a in deduction.keys {
                deduction[a]?.remove(ingredient)
            }
        }
        return result.sorted(by: { $0.allergen < $1.allergen })
            .map(\.ingredient.string)
            .joined(separator: ",")
    }
}

extension Day21.Food: PatternConvertible {
    static let pattern: String = "(.*) \\(contains (.*)\\)"

    init(match: PatternMatch) {
        self.init(
            ingredients: Set(
                match.string(at: 0).components(separatedBy: " ").map(Day21.Ingredient.init)
            ),
            allergens: Set(
                match.string(at: 1).components(separatedBy: ", ").map(Day21.Allergen.init)
            )
        )
    }
}
