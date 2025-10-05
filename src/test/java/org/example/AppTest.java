package org.example;

import org.example.metrics.PerformanceTracker;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    private InsertionSort sorter() {
        return new InsertionSort(true, true, true);
    }

    @Test
    void handlesEmptyAndSingle() {
        int[] a = {};
        sorter().sort(a, new PerformanceTracker());
        assertArrayEquals(new int[]{}, a);

        int[] b = {7};
        sorter().sort(b, new PerformanceTracker());
        assertArrayEquals(new int[]{7}, b);
    }

    @Test
    void handlesDuplicates() {
        int[] a = {3, 1, 2, 1, 2, 3};
        sorter().sort(a, new PerformanceTracker());
        assertArrayEquals(new int[]{1, 1, 2, 2, 3, 3}, a);
    }

    @Test
    void handlesSortedAndReversed() {
        int[] s = {1, 2, 3, 4, 5};
        int[] r = {5, 4, 3, 2, 1};
        sorter().sort(s, new PerformanceTracker());
        sorter().sort(r, new PerformanceTracker());
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, s);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, r);
    }

    @Test
    void propertyBasedRandom() {
        Random rnd = new Random(1);
        for (int t = 0; t < 50; t++) {
            int n = 1 + rnd.nextInt(300);
            int[] a = rnd.ints(n).toArray();
            int[] expected = Arrays.copyOf(a, n);
            Arrays.sort(expected);

            sorter().sort(a, new PerformanceTracker());
            assertArrayEquals(expected, a);
        }
    }
}
