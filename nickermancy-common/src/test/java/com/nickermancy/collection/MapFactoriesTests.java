package com.nickermancy.collection;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MapFactoriesTests {

    @Test
    void testHashMapFactory() {
        assertThat(MapFactories.mapOf())
            .isExactlyInstanceOf(HashMap.class).hasSize(0);
        assertThat(MapFactories.mapOf(1L, "one"))
            .isExactlyInstanceOf(HashMap.class).hasSize(1)
            .containsExactlyInAnyOrderEntriesOf(Map.of(1L, "one"));
        assertThat(MapFactories.mapOf(1L, "one", 2L, "two"))
            .isExactlyInstanceOf(HashMap.class).hasSize(2)
            .containsExactlyInAnyOrderEntriesOf(Map.of(1L, "one", 2L, "two"));
        assertThat(MapFactories.mapOf(1L, "one", 2L, "two", 3L, "three"))
            .isExactlyInstanceOf(HashMap.class).hasSize(3)
            .containsExactlyInAnyOrderEntriesOf(Map.of(1L, "one", 2L, "two", 3L, "three"));
        assertThat(MapFactories.mapOf(1L, "one", 2L, "two", 3L, "three", 4L, "four"))
            .isExactlyInstanceOf(HashMap.class).hasSize(4)
            .containsExactlyInAnyOrderEntriesOf(Map.of(1L, "one", 2L, "two", 3L, "three", 4L, "four"));
        assertThat(MapFactories.mapOf(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five"))
            .isExactlyInstanceOf(HashMap.class).hasSize(5)
            .containsExactlyInAnyOrderEntriesOf(Map.of(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five"));
        assertThat(MapFactories.mapOf(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six"))
            .isExactlyInstanceOf(HashMap.class).hasSize(6)
            .containsExactlyInAnyOrderEntriesOf(Map.of(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six"));
        assertThat(MapFactories.mapOf(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six", 7L, "seven"))
            .isExactlyInstanceOf(HashMap.class).hasSize(7)
            .containsExactlyInAnyOrderEntriesOf(Map.of(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six", 7L, "seven"));
        assertThat(MapFactories.mapOf(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six", 7L, "seven", 8L, "eight"))
            .isExactlyInstanceOf(HashMap.class).hasSize(8)
            .containsExactlyInAnyOrderEntriesOf(Map.of(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six", 7L, "seven", 8L, "eight"));
        assertThat(MapFactories.mapOf(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six", 7L, "seven", 8L, "eight", 9L, "nine"))
            .isExactlyInstanceOf(HashMap.class).hasSize(9)
            .containsExactlyInAnyOrderEntriesOf(Map.of(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six", 7L, "seven", 8L, "eight", 9L, "nine"));
        assertThat(MapFactories.mapOf(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six", 7L, "seven", 8L, "eight", 9L, "nine", 10L, "ten"))
            .isExactlyInstanceOf(HashMap.class).hasSize(10)
            .containsExactlyInAnyOrderEntriesOf(Map.of(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six", 7L, "seven", 8L, "eight", 9L, "nine", 10L, "ten"));
    }

    @Test
    void testLinkedHashMapFactory() {
        assertThat(MapFactories.linkedMapOf())
            .isExactlyInstanceOf(LinkedHashMap.class).isEmpty();
        assertThat(MapFactories.linkedMapOf(1L, "one"))
            .isExactlyInstanceOf(LinkedHashMap.class).hasSize(1)
            .containsExactlyInAnyOrderEntriesOf(Map.of(1L, "one"));
        assertThat(MapFactories.linkedMapOf(1L, "one", 2L, "two"))
            .isExactlyInstanceOf(LinkedHashMap.class).hasSize(2)
            .containsExactlyInAnyOrderEntriesOf(Map.of(1L, "one", 2L, "two"));
        assertThat(MapFactories.linkedMapOf(1L, "one", 2L, "two", 3L, "three"))
            .isExactlyInstanceOf(LinkedHashMap.class).hasSize(3)
            .containsExactlyInAnyOrderEntriesOf(Map.of(1L, "one", 2L, "two", 3L, "three"));
        assertThat(MapFactories.linkedMapOf(1L, "one", 2L, "two", 3L, "three", 4L, "four"))
            .isExactlyInstanceOf(LinkedHashMap.class).hasSize(4)
            .containsExactlyInAnyOrderEntriesOf(Map.of(1L, "one", 2L, "two", 3L, "three", 4L, "four"));
        assertThat(MapFactories.linkedMapOf(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five"))
            .isExactlyInstanceOf(LinkedHashMap.class).hasSize(5)
            .containsExactlyInAnyOrderEntriesOf(Map.of(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five"));
        assertThat(MapFactories.linkedMapOf(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six"))
            .isExactlyInstanceOf(LinkedHashMap.class).hasSize(6)
            .containsExactlyInAnyOrderEntriesOf(Map.of(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six"));
        assertThat(MapFactories.linkedMapOf(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six", 7L, "seven"))
            .isExactlyInstanceOf(LinkedHashMap.class).hasSize(7)
            .containsExactlyInAnyOrderEntriesOf(Map.of(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six", 7L, "seven"));
        assertThat(MapFactories.linkedMapOf(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six", 7L, "seven", 8L, "eight"))
            .isExactlyInstanceOf(LinkedHashMap.class).hasSize(8)
            .containsExactlyInAnyOrderEntriesOf(Map.of(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six", 7L, "seven", 8L, "eight"));
        assertThat(MapFactories.linkedMapOf(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six", 7L, "seven", 8L, "eight", 9L, "nine"))
            .isExactlyInstanceOf(LinkedHashMap.class).hasSize(9)
            .containsExactlyInAnyOrderEntriesOf(Map.of(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six", 7L, "seven", 8L, "eight", 9L, "nine"));
        assertThat(MapFactories.linkedMapOf(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six", 7L, "seven", 8L, "eight", 9L, "nine", 10L, "ten"))
            .isExactlyInstanceOf(LinkedHashMap.class).hasSize(10)
            .containsExactlyInAnyOrderEntriesOf(Map.of(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six", 7L, "seven", 8L, "eight", 9L, "nine", 10L, "ten"));

        assertThat(MapFactories.linkedMapOf(1L, "one").keySet())
            .containsExactly(1L);
        assertThat(MapFactories.linkedMapOf(1L, "one", 2L, "two").keySet())
            .containsExactly(1L, 2L);
        assertThat(MapFactories.linkedMapOf(1L, "one", 2L, "two", 3L, "three").keySet())
            .containsExactly(1L, 2L, 3L);
        assertThat(MapFactories.linkedMapOf(1L, "one", 2L, "two", 3L, "three", 4L, "four").keySet())
            .containsExactly(1L, 2L, 3L, 4L);
        assertThat(MapFactories.linkedMapOf(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five").keySet())
            .containsExactly(1L, 2L, 3L, 4L, 5L);
        assertThat(MapFactories.linkedMapOf(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six").keySet())
            .containsExactly(1L, 2L, 3L, 4L, 5L, 6L);
        assertThat(MapFactories.linkedMapOf(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six", 7L, "seven").keySet())
            .containsExactly(1L, 2L, 3L, 4L, 5L, 6L, 7L);
        assertThat(MapFactories.linkedMapOf(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six", 7L, "seven", 8L, "eight").keySet())
            .containsExactly(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
        assertThat(MapFactories.linkedMapOf(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six", 7L, "seven", 8L, "eight", 9L, "nine").keySet())
            .containsExactly(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
        assertThat(MapFactories.linkedMapOf(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six", 7L, "seven", 8L, "eight", 9L, "nine", 10L, "ten").keySet())
            .containsExactly(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L);

        assertThat(MapFactories.linkedMapOf(1L, "one").values())
            .containsExactly("one");
        assertThat(MapFactories.linkedMapOf(1L, "one", 2L, "two").values())
            .containsExactly("one", "two");
        assertThat(MapFactories.linkedMapOf(1L, "one", 2L, "two", 3L, "three").values())
            .containsExactly("one", "two", "three");
        assertThat(MapFactories.linkedMapOf(1L, "one", 2L, "two", 3L, "three", 4L, "four").values())
            .containsExactly("one", "two", "three", "four");
        assertThat(MapFactories.linkedMapOf(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five").values())
            .containsExactly("one", "two", "three", "four", "five");
        assertThat(MapFactories.linkedMapOf(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six").values())
            .containsExactly("one", "two", "three", "four", "five", "six");
        assertThat(MapFactories.linkedMapOf(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six", 7L, "seven").values())
            .containsExactly("one", "two", "three", "four", "five", "six", "seven");
        assertThat(MapFactories.linkedMapOf(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six", 7L, "seven", 8L, "eight").values())
            .containsExactly("one", "two", "three", "four", "five", "six", "seven", "eight");
        assertThat(MapFactories.linkedMapOf(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six", 7L, "seven", 8L, "eight", 9L, "nine").values())
            .containsExactly("one", "two", "three", "four", "five", "six", "seven", "eight", "nine");
        assertThat(MapFactories.linkedMapOf(1L, "one", 2L, "two", 3L, "three", 4L, "four", 5L, "five", 6L, "six", 7L, "seven", 8L, "eight", 9L, "nine", 10L, "ten").values())
            .containsExactly("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten");
    }
}
