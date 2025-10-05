package org.example;

import org.example.metrics.PerformanceTracker;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;


public class App {

    private static final Random RNG = new Random(123);

    public static void main(String[] args) throws Exception {
        String distribution = argOr(args, 0, "random");
        int n = Integer.parseInt(argOr(args, 1, "1000"));
        int trials = Integer.parseInt(argOr(args, 2, "3"));

        File outDir = new File("docs/performance-plots");
        outDir.mkdirs();
        File csv = new File(outDir, "results.csv");

        boolean writeHeader = !csv.exists();

        try (PrintWriter pw = new PrintWriter(new FileWriter(csv, true))) {
            if (writeHeader) {
                pw.println(PerformanceTracker.csvHeader());
            }

            for (int t = 1; t <= trials; t++) {
                int[] base = makeArray(n, distribution);
                int[] a = Arrays.copyOf(base, base.length);

                PerformanceTracker mt = new PerformanceTracker();

                InsertionSort sorter = new InsertionSort(true, true, true);
                sorter.sort(a, mt);

                System.out.printf(
                        "trial=%d dist=%s n=%d time=%dns comps=%d swaps=%d reads=%d writes=%d%n",
                        t, distribution, n, mt.getNanos(), mt.getComparisons(),
                        mt.getSwaps(), mt.getArrayReads(), mt.getArrayWrites()
                );

                pw.println(mt.toCsvRow("insertion", distribution, n, t));
            }
        }
    }

    private static String argOr(String[] a, int idx, String def) {
        return (idx < a.length && a[idx] != null && !a[idx].isBlank()) ? a[idx] : def;
    }

    static int[] makeArray(int n, String dist) {
        int[] arr = new int[n];
        switch (dist.toLowerCase()) {
            case "sorted" -> {
                for (int i = 0; i < n; i++) arr[i] = i;
            }
            case "reversed" -> {
                for (int i = 0; i < n; i++) arr[i] = n - i;
            }
            case "nearly" -> {
                for (int i = 0; i < n; i++) arr[i] = i;
                int swaps = Math.max(1, n / 20); // ~5%
                for (int k = 0; k < swaps; k++) {
                    int i = RNG.nextInt(n), j = RNG.nextInt(n);
                    int tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
                }
            }
            default -> { // random
                for (int i = 0; i < n; i++) arr[i] = RNG.nextInt();
            }
        }
        return arr;
    }
}
