//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

private let regex = Regex("Step (.) must be finished before step (.) can begin\\.")

final class Day7: Solver {

    private final class Step {
        let id: String

        var dependencies: [Step] = []
        var isDone = false

        var isReady: Bool {
            return dependencies.allSatisfy(^\.isDone)
        }

        var duration: Int {
            let dynamic = Int(id.unicodeScalars.first!.value)
            return 60 + (dynamic - 64)
        }

        init(id: String) {
            self.id = id
        }
    }

    private final class Task {
        let step: Step
        let startTime: Int

        init(step: Step, startTime: Int) {
            self.step = step
            self.startTime = startTime
        }

        func hasHadEnoughTime(at time: Int) -> Bool {
            return (time - startTime) >= step.duration
        }
    }

    private final class ParallelWorkingState {
        let workers: Int = 5
        var time: Int = 0

        var steps: [Step] = []
        var tasks: [Task] = []

        var currentTasks: [Task] {
            return tasks.filter { !$0.step.isDone }
        }

        private var nextReadyStep: Step? {
            return steps.first(where: { step in
                return !step.isDone
                    && step.isReady
                    && !tasks.contains(where: { $0.step === step })
            })
        }

        var isDone: Bool {
            return tasks.count == steps.count
                && tasks.allSatisfy { $0.step.isDone }
        }

        func markTasksAsCompleted() {
            currentTasks
                .filter { $0.hasHadEnoughTime(at: time) }
                .forEach { $0.step.isDone = true }
        }

        func dispatchTaskIfPossible() {
            while currentTasks.count < workers, let step = nextReadyStep {
                let task = Task(step: step, startTime: time)
                tasks.append(task)
            }
        }
    }

    func solveFirst(input: Input) throws -> String {
        let steps: [Step] = input
            .lines()
            .reduce(into: [String: Step](), readStep)
            .values
            .sorted(by: { $0.id < $1.id })

        var completionOrder: [String] = []
        while let index = steps.firstIndex(where: { $0.isReady && !$0.isDone }) {
            completionOrder.append(steps[index].id)
            steps[index].isDone = true
        }

        return completionOrder.joined()
    }

    func solveSecond(input: Input) throws -> String {
        let state = ParallelWorkingState()
        state.steps = input
            .lines()
            .reduce(into: [String: Step](), readStep)
            .values
            .sorted(by: { $0.id < $1.id })

        while !state.isDone {
            state.markTasksAsCompleted()

            state.dispatchTaskIfPossible()

            state.time += 1
        }

        return (state.time - 1).description
    }

    private func readStep(into steps: inout [String: Step], extractedFrom line: String) {
        let match = regex.matchOne(in: line)!
        let id1 = match.capturedValue(at: 1, type: String.self)!
        let id2 = match.capturedValue(at: 2, type: String.self)!

        let step1 = steps[id1] ?? Step(id: id1)
        let step2 = steps[id2] ?? Step(id: id2)
        step2.dependencies.append(step1)

        steps[id1] = step1
        steps[id2] = step2
    }
}
