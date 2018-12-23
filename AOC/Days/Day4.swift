//
//  Copyright © 2018 Fredrik Bystam. All rights reserved.
//

import Foundation

final class Day4: Solver {

    fileprivate struct Event {
        let guardId: String
        //let time: Date
        let content: EventContent
    }

    fileprivate enum EventContent {
        case beganShift
        case fellAsleep(minute: Int)
        case wokeUp(minute: Int)
    }

    fileprivate struct SleepStats {
        let totalMinutes: Int
        let mostPopularMinute: Int
        let mostPopularMinuteTime: Int
    }

    func solveFirst(input: Input) throws -> String {
        var guardId: String = ""
        return input.lines()
            .sorted()
            .map { Event.parse(line: $0, guardId: &guardId) }
            .grouped(by: ^\.guardId)
            .mapValues(sleepStats)
            .max(by: { $0.value.totalMinutes < $1.value.totalMinutes })
            .map { (guardId, stats) in parseInt(guardId) * stats.mostPopularMinute }!
            .description
    }

    func solveSecond(input: Input) throws -> String {
        var guardId: String = ""
        return input.lines()
            .sorted()
            .map { Event.parse(line: $0, guardId: &guardId) }
            .grouped(by: ^\.guardId)
            .mapValues(sleepStats)
            .max(by: { $0.value.mostPopularMinuteTime < $1.value.mostPopularMinuteTime })
            .map { (guardId, stats) in parseInt(guardId) * stats.mostPopularMinute }!
            .description
    }

    private func sleepStats(in events: [Event]) -> SleepStats {
        assert(Set(events.map(^\.guardId)).count == 1)

        var sleepStart = 0
        let sleepRanges: [Range<Int>] = events.reduce(into: [], { acc, event in
            switch event.content {
            case .beganShift: break
            case .fellAsleep(let minute): sleepStart = minute
            case .wokeUp(let minute): acc.append(sleepStart..<minute)
            }
        })

        let totalMinutes: Int = sleepRanges.reduce(into: 0, { $0 += $1.count })
        let mostPopularMinute: (Int, Int)? = sleepRanges
            .map(Array.init)
            .joined()
            .countedOccurrences()
            .max(by: { $0.value < $1.value })

        return SleepStats(totalMinutes: totalMinutes,
                          mostPopularMinute: mostPopularMinute?.0 ?? 0,
                          mostPopularMinuteTime: mostPopularMinute?.1 ?? 0)
    }

}

private let lineRegex = Regex("\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:(\\d{2})\\] (.+)")
private let beingShiftRegex = Regex("Guard #(\\d+) begins shift")

extension Day4.Event {

    static func parse(line: String, guardId: inout String) -> Day4.Event {
        let lineMatch = lineRegex.matchOne(in: line)!
        let eventString = lineMatch.capturedValue(at: 2, type: String.self)!

        if let beginShiftMatch = beingShiftRegex.matchOne(in: eventString) {
            let id = beginShiftMatch.capturedValue(at: 1, type: String.self)!
            guardId = id
            return Day4.Event(guardId: id, content: .beganShift)
        } else if eventString == "falls asleep" {
            return Day4.Event(guardId: guardId, content: .fellAsleep(minute: lineMatch.capturedValue(at: 1)!))
        } else if eventString == "wakes up" {
            return Day4.Event(guardId: guardId, content: .wokeUp(minute: lineMatch.capturedValue(at: 1)!))
        } else {
            fatalError("Line not matched: \(line)")
        }
    }
}
