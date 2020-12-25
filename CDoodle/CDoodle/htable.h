//
//  htable.h
//  CDoodle
//
//  Created by Fredrik Bystam on 2020-12-24.
//

#ifndef htable_h
#define htable_h

#include <string.h>

// MARK: - Fill out model

static int int_hash(int value) {
    return value;
}

static char int_eq(int i1, int i2) {
    return i1 == i2;
}

#define htable_key int
#define htable_value char *
static int(*htable_key_hash)(htable_key) = &int_hash;
static char(*htable_key_eq)(htable_key, htable_key) = &int_eq;

// MARK: - End fill out model

typedef struct _htable_entry {
    char populated;
    htable_key key;
    htable_value value;
} htable_entry;

typedef struct _htable {
    int length;
    htable_entry *head;
} htable;

htable htable_create(int length);
void htable_destroy(htable table);
htable_entry htable_get(htable table, htable_key key);
void htable_put(htable table, htable_key key, htable_value value);
void htable_delete(htable table, htable_key key);

#endif /* htable_h */
