//
//  htable.c
//  CDoodle
//
//  Created by Fredrik Bystam on 2020-12-24.
//

#include "htable.h"
#include <stdlib.h>

htable_entry *_entry_forkey(htable table, htable_key key);

htable htable_create(int length) {
    htable table;
    table.length = length;
    table.head = calloc(length, sizeof(htable_entry));
    return table;
}

void htable_destroy(htable table) {
    free(table.head);
}

htable_entry htable_get(htable table, htable_key key) {
    return *_entry_forkey(table, key);
}

void htable_put(htable table, htable_key key, htable_value value) {
    htable_entry *ptr = _entry_forkey(table, key);
    ptr->populated = 1;
    ptr->key = key;
    ptr->value = value;
}

void htable_delete(htable table, htable_key key) {
    htable_entry *ptr = _entry_forkey(table, key);
    memset(ptr, 0, sizeof(htable_entry));
}

htable_entry *_entry_forkey(htable table, htable_key key) {
    int offset = abs(htable_key_hash(key)) % table.length;
    htable_entry *ptr;
    do {
        ptr = &table.head[offset];
        offset = (offset + 1) % table.length;
    } while (ptr->populated != 0 && htable_key_eq(key, ptr->key) == 0);
    return ptr;
}
