//
//  main.c
//  CDoodle
//
//  Created by Fredrik Bystam on 2020-12-24.
//

#include <stdio.h>

#include "htable.h"

int main(int argc, const char * argv[]) {
    htable table = htable_create(100);
    htable_put(table, 5, "Hello");
    htable_put(table, 105, "Hello two");
    printf("%s\n", htable_get(table, 5).value);
    printf("%s\n", htable_get(table, 105).value);
    return 0;
}
