//
//  Model.c
//  CDoodle
//
//  Created by Fredrik Tõnisson-Bystam on 2020-12-25.
//

#include <stdio.h>

#include "Model.h"

const Point2 Point2_origin = { 0, 0 };

int Point2_hash(Point2 p) {
    return p.x | (p.y << 16);
}

bool Point2_eq(Point2 p1, Point2 p2) {
    return p1.x == p2.x && p1.y == p2.y;
}
