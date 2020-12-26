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

htable_entry *htable_get_ptr(htable table, htable_key key) {
    return _entry_forkey(table, key);
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

    // rehash used entries after
    while ((++ptr)->populated) {
        htable_entry entry = *ptr;
        memset(ptr, 0, sizeof(htable_entry));
        htable_put(table, entry.key, entry.value);
    }
}

htable_entry *_entry_forkey(htable table, htable_key key) {
    int offset = abs(htable_key_hash(key)) % table.length;
    const int start = offset;
    htable_entry *ptr;
    do {
        ptr = &table.head[offset];
        offset = (offset + 1) % table.length;
        if (offset == start) {
            return NULL;
        }
    } while (ptr->populated != 0 && htable_key_eq(key, ptr->key) == 0);
    return ptr;
}

int htable_count(htable table) {
    int count = 0;
    htable_iter iter = htable_iter_make(table);
    while (htable_iter_next(&iter)) {
        count += 1;
    }
    return count;
}

htable_iter htable_iter_make(htable table) {
    htable_iter iter;
    iter.ptr = table.head;
    iter.distance = -1;
    iter.end = table.length;
    return iter;
}

bool htable_iter_next(htable_iter *iter) {
    do {
        if (iter->distance != -1) {
            iter->ptr += 1;
        }
        iter->distance += 1;
    }
    while (iter->distance < iter->end && iter->ptr->populated == 0);

    return iter->distance < iter->end;
}
