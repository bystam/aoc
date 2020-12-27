//
//  htable.h
//  CDoodle
//
//  Created by Fredrik Bystam on 2020-12-24.
//

#ifndef htable_h
#define htable_h

#include <string.h>
#include <stdbool.h>
#include "Model.h"

// MARK: - Fill out model

#define htable_key Point2
#define htable_value int
static int(*htable_key_hash)(htable_key) = &Point2_hash;
static bool(*htable_key_eq)(htable_key, htable_key) = &Point2_eq;

// MARK: - End fill out model

typedef struct _htable_entry {
    bool populated;
    htable_key key;
    htable_value value;
} htable_entry;

typedef struct _htable {
    int length;
    htable_entry *head;
} htable;

typedef struct _htable_iter {
    htable_entry *ptr;
    int distance;
    int end;
} htable_iter;

htable htable_create(int length);
void htable_destroy(htable table);
void htable_clear(htable table);
htable_entry htable_get(htable table, htable_key key);
htable_entry *htable_get_ptr(htable table, htable_key key);
void htable_put(htable table, htable_key key, htable_value value);
void htable_delete(htable table, htable_key key);
int htable_count(htable table);

htable_iter htable_iter_make(htable table);
bool htable_iter_next(htable_iter *iter);

#endif /* htable_h */
