//
//  main.c
//  CDoodle
//
//  Created by Fredrik Bystam on 2020-12-24.
//

#include <stdio.h>
#include <assert.h>

#include "htable.h"

const char *_input[];

htable walkToinitialState(void);
void simulate100days(htable initial);
void countNeighbor(htable neighbors, Point2 point);

int main(int argc, const char * argv[]) {
    htable black = walkToinitialState();

    printf("First: %d\n", htable_count(black));

    simulate100days(black);

    printf("Second: %d\n", htable_count(black));

    return 0;
}

htable walkToinitialState(void) {
    htable table = htable_create(10000);

    for (int li = 0;; ++li) {
        const char *line = _input[li];
        if (line[0] == 'X') {
            return table;
        }
        Point2 point = Point2_origin;

        int ci = 0;
        while (line[ci] != '\0') {
            switch (line[ci]) {
                case 's': {
                    if (line[ci + 1] == 'e') {
                        point.x += 1;
                        point.y += 1;
                    } else if (line[ci + 1] == 'w') {
                        point.x -= 1;
                        point.y += 1;
                    }
                    ci += 2;
                    break;
                }
                case 'n': {
                    if (line[ci + 1] == 'e') {
                        point.x += 1;
                        point.y -= 1;
                    } else if (line[ci + 1] == 'w') {
                        point.x -= 1;
                        point.y -= 1;
                    }
                    ci += 2;
                    break;
                }
                case 'e': {
                    point.x += 2;
                    ci += 1;
                    break;
                }
                case 'w': {
                    point.x -= 2;
                    ci += 1;
                    break;
                }
            }
        }

        htable_entry entry = htable_get(table, point);
        if (entry.populated == true) {
            htable_delete(table, point);
        } else {
            htable_put(table, point, 1);
        }
    }
}

void simulate100days(htable black) {
    htable neighbors = htable_create(100000);
    for (int i = 0; i < 100; ++i) {

        htable_iter iter = htable_iter_make(black);
        while (htable_iter_next(&iter)) {
            htable_put(neighbors, iter.ptr->key, 0);
        }
        iter = htable_iter_make(black);
        while (htable_iter_next(&iter)) {
            Point2 p = iter.ptr->key;
            countNeighbor(neighbors, (Point2){ p.x + 2, p.y }); // east
            countNeighbor(neighbors, (Point2){ p.x - 2, p.y }); // west
            countNeighbor(neighbors, (Point2){ p.x + 1, p.y + 1 }); // south-east
            countNeighbor(neighbors, (Point2){ p.x - 1, p.y + 1 }); // south-west
            countNeighbor(neighbors, (Point2){ p.x + 1, p.y - 1 }); // north-east
            countNeighbor(neighbors, (Point2){ p.x - 1, p.y - 1 }); // north-west
        }

        iter = htable_iter_make(neighbors);
        while (htable_iter_next(&iter)) {
            Point2 p = iter.ptr->key;
            int count = iter.ptr->value;
            bool isBlack = htable_get(black, p).populated;

            if (isBlack) {
                if (count == 0 || count > 2) {
                    htable_delete(black, p);
                }
            } else {
                if (count == 2) {
                    htable_put(black, p, 1);
                }
            }
        }

        htable_clear(neighbors);
    }
    htable_destroy(neighbors);
}

inline void countNeighbor(htable neighbors, Point2 point) {
    htable_entry entry = htable_get(neighbors, point);
    htable_put(neighbors, point, entry.populated ? entry.value + 1 : 1);
}

const char *_input[] = {
    "swswnwnesewswsesesewwwwnwwswnewww",
    "senenwseeneenwnwnenesweweeswneenee",
    "swswswnwwseneneseswswneswswswnewswwwsw",
    "neswnenwneewnenwnwnwnenwnenenenenese",
    "wswwswwswwswnewwswwwwsenenewswe",
    "swswnewswneswwneswswswswswswswswswswsw",
    "neswswwswneeswwswswnenwswswswswwswswswsw",
    "nwseneseseseeswswswwswewsw",
    "sesewseswwswseneswneseneneeswswswseenw",
    "nwwwwwnwwwwnwwewwsenw",
    "neeeswseseeseenenewwnwwne",
    "swwnwnwesweswswswnesenweswwswswswse",
    "eswsenwnwnwseseesesenw",
    "senwenwwnwnwnwnwnwnenwnwnwsenwnwnwnwnw",
    "senwseeseeseseweweseeseeesesee",
    "ewsenenewneneeneswneneeewnwneneneswe",
    "wnwnwnwnwnwnewnwwwsewnewwweswnww",
    "swwneswswneswswweswswswswwswwswswwsw",
    "seneswwwwswswwneswseswwwswswswswnwsw",
    "nenwseseswneneswnwnenenenwnwwnwseenewnw",
    "wswsesesewseswseenwnenesenwnwswwnee",
    "seeeeesenwseeseesweeeeee",
    "swswwwswwwswseenwswswnweswswnwwsw",
    "eswesweeeeenwenweeeseswnwsew",
    "swnewnwwenwwswnwwwnwnwnwsenwnwnenw",
    "wwwnwwnwwwwwwsew",
    "eweeeeneneenwsweneneeneseneeee",
    "enewseseeseseeseeeew",
    "nweewsesenwenweswenewsweneeee",
    "swwswswnwswseeswswswswsweseseswswswsw",
    "wwwnewwwwenewwseewwwswwwnw",
    "nweenweeesesweenwswseeseeeese",
    "neeneneneneswenwneee",
    "wswswwsenesesesesenwseewswswneseseswnese",
    "wswwenwnwnwsewsenwnwnwnwwnwnwwnwnwwne",
    "wwwsewwwwnwwnwnwnwsewnwsewwnw",
    "sweseswwneneswnwswewwseswenwwneww",
    "seseswwswswwnesewneswnwwswswsenewsw",
    "neeneseeneswneneweenenenwneneneswswnene",
    "swsenwewswswswswseseswswseswnwswseswsese",
    "swswswnwswswswswsenwsenenwswswseswseseswe",
    "eewswenweeeeeeeeeeeenesee",
    "swswswwswwswseseeseeswwseswseeseswsw",
    "swseseseneswseesesewwswnewseseswsesenw",
    "ewwwwwswwwwswsewwnwnesewwww",
    "nwwnwswnwnenwnenwenesenenenwnenwnenenw",
    "swenwnwwnwnwnwewnwswsewsesewnwnwe",
    "wswwwsesewswwnewwsenewwwnwwsww",
    "nwnenewnewneneswnenwsesenenenenewneene",
    "nwnwnwnwnwswenwnenwnwnwnenwnwsenenwnwnw",
    "neseenweeeeeeneweeeeenee",
    "wnenwwnewwsenwswenwnw",
    "nwnwwenwwswswwesenwnwswnwwswenwewse",
    "sesewnwseseswswseswseeswsesesw",
    "eseswseneesenwswswseswswseseswsenwswswsw",
    "sewneenwwsewwweswnwseewnwnesenenw",
    "seeswseeeeeesenwsenwnwswswsesenee",
    "neneeeneswnesenenwneneswnenenwnwnenee",
    "wwswwswnewnwewwwwseswwwwwww",
    "nwswsesweswswswswenwseswnwswswswnwswne",
    "eneseseswswwswnwwswswneseeswseswwese",
    "enenwsenwnwnwnenwnwswnwnwwnenwenwnwnwsw",
    "enwwsenenwnewenwnewnenwnwnenenwnwnwnwnw",
    "eesenesewnenenwswswsewewsenenwswsw",
    "newneseweswneneenenwnenenwnewneneene",
    "nenesewnenenenenwnwseneneneswnesenenenene",
    "neneneneneswneenenenenenenenene",
    "weeeeeweweeeeeeee",
    "nwnwsenwwneswnwnwnenwnenewnenwnwse",
    "eeewewenweeeeesweseeeneeee",
    "neneswneneeneeeneneenenene",
    "nwnwnwnwswwnwswnwnesenwnwneeenwwenwnw",
    "eswwnenenweneenenwwwene",
    "swswnwswsenwwewswsenweseeseenwswne",
    "swnwwwnwwnweswwenwwenwnwswne",
    "nwnwnenwnwnwnwnwnwenwsenwnwnenwweswnw",
    "nwnwnwenwnwnwswswnweenwewnwnewswwee",
    "esweenweeeeeeeesweeneeeneesw",
    "eeenenwswesweeeenwnwseeeseese",
    "swwnwnwewwwwwwenwwwwenwwnw",
    "nweswswswswsweswseswswwneseswswsenenwsw",
    "seseseswseseseseseseswsesesesenw",
    "eweneenenenweswseeeeesweeseew",
    "sesesesesesenwsesesesese",
    "senenewnwnenenesene",
    "ewnenenewwneeeeeeeneneeneneswnw",
    "senewsesesesesesewseseswseseseseswsee",
    "seswseneneseswwwswweswne",
    "swneseseseseswwsenesesenwseseseseseswse",
    "seseseseseeseseseeswesesewenwnenwsenw",
    "wwnwswnwwwwewswwnwenweneswww",
    "neswenenwnwnwneneenwnenwnwsenenwnenwwne",
    "wneswsweseesenwswwswswswswwsenwneseswsw",
    "nwnwwsewwnenesesewswswnwneeseeew",
    "swswswswswnwewwswseswswswnewsweswww",
    "wnewnewnwwwesewwse",
    "nwswwswneswswseswewswnwswsewwswwswswsw",
    "wswnenenwwsesesewwnwenwnweswnesenww",
    "nwnwnwnwnwwswenwneswswnwnenwenwnwnee",
    "swseswnwswswswswswswswsweswswswswswnwsw",
    "wseseneswseseswswswseswsesw",
    "nwwnwnwnwwwsesenenwwnwnwnwwseenenwsenw",
    "nwswnwnwnwnewenwsenwnwnwnwnwnenenenenene",
    "eswseewneeeeneeseeeeeenwnee",
    "nwseeeneneeenesenenesenewnewweee",
    "wwswewnwewswwsesewnenewnewwww",
    "nenenenwwneneswneneneneenenenwnenewe",
    "sesesesewseseseswsesenenwseseeswsenwsesese",
    "nenwswweenewnenenenenwswnenenenweswne",
    "nwnenwwnwnwnenwnwnwnwnwnwnwnwswnesenwnw",
    "nwnwnwswenwenwnwnwnwnwwnwnwnwnwswnww",
    "sweswswswswsweswwswswswnwswswwswswwnw",
    "seseeeneeswnweneneswweeeweseew",
    "seswnwneseswwseswswenenwwswwswswewne",
    "eneenwseswseeeeeeweeeeeeese",
    "eeswsenwseesweswsesenweenwnwneeesese",
    "swnenwnenwnwnwnwnwneneneneneenenesesene",
    "wseseeeneswneenwneewe",
    "wnwwwwseenwwwswwwwswwwwww",
    "nenenwnwsenwnenenwnwnwnesenewwnenwnwnw",
    "eseeswweseneseseseswswseeeenwnwnwe",
    "newneneneneneneneneneenwnenesw",
    "seswseswseswseseseneseseswswsesenesw",
    "wswneswswswnwwswswseswswneesweswswswsw",
    "nenenewneneeneneneenenenee",
    "nesewseeseneeseseseseeseseseewsee",
    "newnwneneenwneneneneenewwseswesese",
    "sewwwwnewnewswwneswwwneswwwe",
    "nwwwnwseewwwwwwwwwnewswwsew",
    "wewwswswswwswswwwswwneewwwnw",
    "senwwswenwsenwewswsw",
    "sweeeswneeeeeeenweeeweee",
    "swswswswswwewweewwseswnwnwswww",
    "neneenwswnwwsesenenwnwnenwnenenwswswnw",
    "seeseneswswswwswesesenwswneswseseswsw",
    "swswnwnwenenenwwne",
    "eneenwneneneneseneeeeeneeeeswnw",
    "swswwwwswswswwwsenewnewseswe",
    "sesweseenewseeneseesenwseeesesewsw",
    "swswseswneewswswswwnwswnwswswsesw",
    "eseneseseseseseeeeswse",
    "neneswneneenewewneneneesesenenewne",
    "nenwnwnwneswnwnwnenwnwnenwnw",
    "wwnwwwsenwwwwnenewsewwwwnwnw",
    "neeenesenenewneseneneswneneenenenwne",
    "swswnwnenenwnenwnenwnenenwneenenwnwnenw",
    "wwwwwnewwwwwwwwwwse",
    "nwseswwswsewnwwewseswwneswswswswnwswne",
    "senenenenwnwnenwnenwwwnwnenwenw",
    "nwnwwsenwnwwwnwnwwnwnwwwwnw",
    "ewnwwnwwswwwwsenwsewnwewwnwnww",
    "seeenwsenewswneswneenenwswneeneenee",
    "eeneeswneeeneeswneneeenweeee",
    "nwwsewnwnwnewseswnewneseenenewenwnw",
    "wnwnwwwwwwwwwwwnesewnwnwswnwe",
    "wnwswenwnwnwnewnwnwnenenwnwswwsenewnwse",
    "nwwneenwnwswswswseswnwneenenw",
    "senwwnwewwseseneenenwnew",
    "eeweeeseeeesesenwwewneeew",
    "sweeseeeeeeeeeesenwenweweee",
    "nwnwnwnewsenwnwswnwenwnwsewnwnwwnwnw",
    "wswswneswseneswswswsw",
    "nwwnwnwnwnwnwnwwnwnwnwwwsenwnwwnese",
    "seseswseseswsenwseseseseneneseswswnewsesw",
    "nwwwsenwwnenwswwwewneenewwswswnww",
    "nenenwswnenenenesenewnenenesenenenenene",
    "swwnwwseswenwwwwwswswneswwseswe",
    "wwwnesewwswwnewswne",
    "swseswswswswwswnwseswneswswswneswswsesesw",
    "wswwewwnewwwseswnewewwwwwenw",
    "swswswsweswswwswswswsweswswnwnweswswwsw",
    "nwnwnwnwwswnwnwwnwnwnwnwnwnwewnw",
    "nwenenwnenwweneneseneswenewwnewne",
    "wswnwswnwseswswseseeswswsesweswswswsw",
    "seeseswswswsesesenwseseseewnwswswsenesw",
    "weesenesesesesesesesese",
    "wwnewwwnwseewswewswswwsenesene",
    "esewseweeneeeseeeeeseeeee",
    "swseswwwswswwsewnwwnwneweswwwnesw",
    "nwnwswswswswwneewnwewswsweswewe",
    "wseswswwnenwesesesewnewnwsenewwne",
    "neswswwwswswswswsweswwwswneeswesw",
    "neseswsewsenwseswswswseseseneneswswswsesw",
    "senwwnwnwnenwnwnwnwnwsenwnwnwnwnwnwnwnw",
    "neewwswwwwsewswwwwswswswwww",
    "seswseseneseesesesenewseseseseseewse",
    "nwesesesesenwseseseseeseseneseseseswse",
    "swswseswswswswweneswwswswswwwnwnew",
    "nwneseswenenwnenenene",
    "swnwseeswnwseneswnwwswswswseeswswswswsese",
    "wwwwwesenwswwswwewswswwwnwww",
    "neneswsesewseswswswsesese",
    "neneenenwneneneneneneneseswnenwnwnenene",
    "eseeseseswnwnenwswseseneseseeswseseesese",
    "seeseseswseweeeeeneseneeswenwse",
    "nwnwsenenenwnenenwne",
    "nwsweeswswswswswewseswswswswswswnwswsw",
    "neeeeneseeenweeseeneweneswsew",
    "seswenwswwseeseswseeseseseswwswsese",
    "wnewwwweewwsenwwnwswnwswweseswse",
    "eneseswneeneneneneeneneeeneenenw",
    "nwwwsewwwnweneswenwwwnwsenwww",
    "wneswswswswwwwwwwswswswsesw",
    "senwnenwnwnwnwnwnenenwswnwnwnwnwnwnwnww",
    "swnwswnwswswnwswwsweeswswwswnewsesw",
    "enwnwnwewnwwnwenwsenwwneswneseese",
    "wswswwswswswwwneswswwwww",
    "neswnwneenwnenenenenenewswneneswsenwnw",
    "eeeeeesenwnweseneeenwseeeewswse",
    "swnwnenweneneneenwnwnwneseswnenew",
    "nwneneweeeseesewneneeswneneeeenene",
    "neeeeeeeeseseeweeese",
    "nwnewnwwnenwwwnwsenwsenwsenwnwwnwsese",
    "swwesweswswwswwwwswsewwwswneewsw",
    "sweneeneenenenwnenwneesw",
    "wwwwswwseswwwswwneneneww",
    "eseneseeswnwswneweeeenwnenenewne",
    "seseenesewsewnesesesesesesewseseesw",
    "wswswseswswwneeswseseseseeswnwseesese",
    "nenwnenwwnesenenenwneneswseesene",
    "seenesewseseseseeseneweeeeeesese",
    "eneenenenenweeswneeswenesweeene",
    "nwsweneewneswneneneeeenwswwswwnese",
    "eeeeeweeeweeseneeeeswee",
    "eswnwwseswseswswseseeew",
    "sesesesesesesesenwewswseneseseseseseee",
    "seneneenwnwnenwneneswnenwnwsewwnwnwnw",
    "wwwsewswwnwwnenwneswwseenwnwnese",
    "nwenweswwwnewseswswswswnesweswswesw",
    "nwswswswswweswseeswswswneswseswsewswe",
    "seswswnwneesweswswnewnwseswwnwsewnwsw",
    "wwwwwwnwswneswswsweswwnwewwwswsw",
    "nenwnwswwnwnwenwnwnwswnwwnwneswnw",
    "nwnwnwnwwwnwseneenwnwseenwnwwnwswnwnw",
    "wwnwwwwwwswwnwwe",
    "nwnwnwnwnwnwnwneswnwnwswnwenwenenenw",
    "nwnwnwnesenwnwsenweswnwsewneneseeswsw",
    "sesesesesesesesenwseseeeeseswwsesese",
    "seseeweeweneswnenwseeeese",
    "enenwneseeeswewnw",
    "sweswswnwswswswneswswswswswswswsesese",
    "seeeeenewwneeseeene",
    "nesenwnenwnwnwnewnwnenenwene",
    "seweeseeseseeseseseeseewsesenew",
    "senwswswnewswswswneseneswswwswswseswwsw",
    "nwneneneenenenwneneneswnwnenenenesw",
    "eseswswseswswswnwswswsenwswseswseneswswsw",
    "weseeeseseeseseseeseewsesewsesese",
    "seseswswsesesenwswseseseswsw",
    "nwnenwewwnwswnwenwseswwnwnwnwenenwe",
    "swseneseseswswsesenewswnwnwneesenese",
    "wesewnwwneswwwwwwwwwwnwwswnw",
    "swnwwsenwseswnenewwwswseswnwswswesw",
    "swsesewnwnwnwnwnwnwwnwnenesesenwnenwnww",
    "neneswneseenwnenewnenenenenwsewnwwnenenw",
    "wwwwwwwwswwwnewwwwwe",
    "swwswswewswswwwwww",
    "seswewewnenweseeneneeewnwewnw",
    "senwnwwnwneneenwnesenenewsenwwnenwnesw",
    "seswseweseneneneesesweeeeeeeesw",
    "eenweseneesweeesweseeeneneswee",
    "swswsenwsenwseseswseseeseseseswsesesew",
    "nwnwnwwnwnenenwnwnwnwwenwnwnwnenwsenw",
    "neseesenwseeswsenwsesewe",
    "eewseneesesesenesesesweseseseseese",
    "swswwneswswseswnwswsweswswnwswsweswsw",
    "newnwswwwsewwnwwnwnwnwewnwwwnw",
    "seeeseseneseweeee",
    "seswsweseeseseeenenweeee",
    "enwnwwnwnwnenwnwswwswwnwnwnwnwwe",
    "senwsenwswewweneseeseswwsesesesesese",
    "eeswnwenwswwwnw",
    "nenwswswnenenenesesenwneneneenwenwwe",
    "sewweswsenwweseseeseseseseeeenw",
    "wwnwnenwwwswnwewwwwwswnwwsew",
    "esewenwswswenenweneeenwseeneenwee",
    "nwwnwnwnwnwnwnwnwwnwnwnwe",
    "neswswnesenewswsenwwnweseswswseseseesese",
    "enwnwnwnwwnwnwnwnwnw",
    "swsewneneseeswswewwwnwsewswwnwsw",
    "enwseseewnewwswneswnwsweneswwsesenwse",
    "wnwseswswswswswsweswswenwswswnwseswwsw",
    "enwswseneeeseesee",
    "eenesweeseenweweeeeeenenenw",
    "seseseseswseswseseswsenwwsesenwseseswee",
    "eswweenwseseeseeseeseenenwwee",
    "neneneeenwneeesweeeswweneeseene",
    "neseseseesesesesesesewsewseseesesese",
    "sesenwnwnwnwnwwnwnwsewnwnwnw",
    "neseenenewseeeneneeeweneneesenwene",
    "neeswseeswswneswnewnwseswsweswswwnesw",
    "eenewswseenwneeswewsewnweneneese",
    "sesesweseseswseesewseswsesenesenwsenwsw",
    "eseeeswseeeeeeenweeneenwnwesw",
    "eswneeneswswwneneeeneeeneneenwee",
    "weswseseswswswnwwswswnwnesewwswww",
    "eswseswswnwnwwwswsweswsweswswswswsesw",
    "nwwwnesewnwsesenwnwnwwwnwnwnwwnwww",
    "eweseswwnwewneswwnwwnesesenwenenwse",
    "swnwneswsenewseseseneseswwseseseeswsese",
    "neeseswsesenweeeweeeseneswseseeese",
    "nwnwnenenenewnwnenenwnenwswenwnenwnenwse",
    "wswswwnesweswnewsewnwswswwswswww",
    "neswnenwnenwnenenenenenwnwne",
    "nwnenwnenwnenwswnwenwneswseesenesenwswnw",
    "eseswseseseneneseeswswwsweseenenesese",
    "eseesenenwseseeewswsesesenwseseeewse",
    "swswwswswsweswswswswsesenwswswneswseswsw",
    "eeeseneneseeeseseseesweweeewe",
    "eeeeeeeeeswenewneeeeewe",
    "seeswwswswneseswswneseswseseswseswswsw",
    "nweswnwswswseswseswseswseswswsweswnwsesesw",
    "wnwswwsweswnwswsenewewnewwwewww",
    "eeswswenenwnenenenenenenenenewnw",
    "neswwneeseneswsenenwnwneeswnewwswne",
    "sesewwwneswwswnwwswwnwwseneneew",
    "nwnwnwnwwnwswnwenwnwnwnwnwnwnw",
    "wnewwwnwnewsesesene",
    "sewwnwwnwnwwwnwnenwwenwswwnwewnw",
    "wsewnwwesenwwnwenwwwenwwwnww",
    "nenenwnwsenwnenwnenwnwnenweswnwnwnwnesenw",
    "nenwwnweseswsewsweseeseseneseenwsw",
    "wsweseeseseesenwsenwneesenenwseesw",
    "sesewseneseeseseswesesww",
    "seneeeewwnewwnenweeswnwnesenewsew",
    "nwnwenwnwnwnwnwwswenw",
    "nwnwsewwswswwseneenwnwwnewesenenw",
    "neneneneenenenewsewsenenenenenenenene",
    "nwneswnwnenenwenwnenwseswenenenwswswseswsw",
    "newneneswneneneeneenenesenene",
    "eeeenwneeseneewnenenesweneneeee",
    "nwwnenwswnwewwswesweswwsenenesww",
    "neseneneneswneeenenenwneneneeeewe",
    "newneneeneneneneneneneswnenewnenesenene",
    "wwnwswswswnwwswwwwwewswseseswnwswsw",
    "swwwswswseswswswneewseeswenwswswwnw",
    "swswwwesewsweswwnewwwwwwswwnw",
    "nenwsenenwnwnwswneeneswwenenenwsenwnwsenw",
    "eneneswnewnenewnenenesee",
    "seseseseseseneseseseseesenwsewseeswse",
    "nwseneweeswnwwnw",
    "eeenweenenwenewneneswseeesenwswswne",
    "eenenwesenwesweeswnweneeseneswnw",
    "nenwnwnwswnwswnenenwnenwneneenenenenesw",
    "sesesesesesesesesesesesenwwsesesesesene",
    "sewwwnwswwwwenewwwneswwwwwsww",
    "wwenwesewwnwnwwwwnwwwnwnwwew",
    "wseseswnwswswwneenwwwwsewswswneww",
    "weesesewwseenwswenwswneneesesew",
    "neeswneneenwswnenesenenwnwswnenwnwnene",
    "seeeeeeesweseneenwweeeneenw",
    "enweeenenwswsweenesenenwnwswnesee",
    "swwnesenwswseswswswseswewnwseneseseesw",
    "wwwnwwwnewwewwwwseww",
    "wswnwnwewwnwewwseenwewwsewww",
    "seswseseswswswsewseseseeseswsewsenese",
    "senewnenewewneeneseeseeenwenenw",
    "X"
};
