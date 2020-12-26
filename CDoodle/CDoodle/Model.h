//
//  Model.h
//  CDoodle
//
//  Created by Fredrik Tõnisson-Bystam on 2020-12-25.
//

#ifndef Model_h
#define Model_h

#include <stdbool.h>

typedef struct Point2 {
    int x, y;
} Point2;

extern const Point2 Point2_origin;

int Point2_hash(Point2 value);
bool Point2_eq(Point2 p1, Point2 p2);

#endif /* Model_h */
