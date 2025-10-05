package org.example;

import org.example.metrics.PerformanceTracker;
import java.util.Objects;

/**
 * Insertion Sort с оптимизациями:
 *  - binary search для позиции вставки (меньше сравнений),
 *  - sentinel (перенос минимума в a[0]) — меньше проверок границ,
 *  - сдвиг блока через System.arraycopy (быстрее по константам).
 * Память O(1), стабильный.
 */
public class InsertionSort {

    private final boolean useBinarySearch;
    private final boolean useSentinel;
    private final boolean useArrayCopyShift;

    public InsertionSort(boolean useBinarySearch, boolean useSentinel, boolean useArrayCopyShift) {
        this.useBinarySearch = useBinarySearch;
        this.useSentinel = useSentinel;
        this.useArrayCopyShift = useArrayCopyShift;
    }

    public void sort(int[] a, PerformanceTracker t) {
        Objects.requireNonNull(a, "array must not be null");
        if (a.length < 2) return;

        long start = System.nanoTime();

        int leftBound = 0;
        if (useSentinel) {
            int minIdx = 0;
            for (int i = 1; i < a.length; i++) {
                t.incComparisons();
                t.incRead(); t.incRead();
                if (a[i] < a[minIdx]) minIdx = i;
            }
            swap(a, 0, minIdx, t);
            leftBound = 1; // теперь a[0] — гарантированный минимум
        }

        for (int i = 1; i < a.length; i++) {
            t.incRead();
            int key = a[i];

            // быстрый проход уже отсортированного префикса
            t.incComparisons(); t.incRead();
            if (a[i - 1] <= key) continue;

            int insertPos;
            if (useBinarySearch) {
                insertPos = binarySearchPosition(a, leftBound, i - 1, key, t);
            } else {
                insertPos = i - 1;
                while (insertPos >= leftBound) {
                    t.incComparisons(); t.incRead();
                    if (a[insertPos] <= key) break;
                    insertPos--;
                }
                insertPos++;
            }

            // сдвиг + вставка
            if (useArrayCopyShift) {
                int len = i - insertPos;
                if (len > 0) {
                    t.addReads(len);
                    t.addWrites(len);
                    System.arraycopy(a, insertPos, a, insertPos + 1, len);
                }
                t.incWrite();
                a[insertPos] = key;
            } else {
                for (int j = i; j > insertPos; j--) {
                    t.incRead(); t.incWrite();
                    a[j] = a[j - 1];
                }
                t.incWrite();
                a[insertPos] = key;
            }
        }

        long end = System.nanoTime();
        t.setNanos(end - start);
    }

    private int binarySearchPosition(int[] a, int left, int right, int key, PerformanceTracker t) {
        while (left <= right) {
            int mid = (left + right) >>> 1;
            t.incComparisons(); t.incRead();
            if (a[mid] <= key) left = mid + 1;
            else right = mid - 1;
        }
        return left; // место вставки
    }

    private void swap(int[] a, int i, int j, PerformanceTracker t) {
        if (i == j) return;
        t.incRead(); t.incRead();
        int tmp = a[i];
        t.incWrite();
        a[i] = a[j];
        t.incWrite();
        a[j] = tmp;
        t.incSwaps();
    }
}
