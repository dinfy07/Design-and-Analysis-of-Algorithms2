package org.example.metrics;

public class PerformanceTracker {
    private long comparisons;
    private long swaps;
    private long arrayReads;
    private long arrayWrites;
    private long nanos;

    public void incComparisons() { comparisons++; }
    public void incSwaps() { swaps++; }
    public void incRead() { arrayReads++; }
    public void incWrite() { arrayWrites++; }
    public void addReads(int n) { arrayReads += n; }
    public void addWrites(int n) { arrayWrites += n; }

    public void setNanos(long nanos) { this.nanos = nanos; }

    public long getComparisons() { return comparisons; }
    public long getSwaps() { return swaps; }
    public long getArrayReads() { return arrayReads; }
    public long getArrayWrites() { return arrayWrites; }
    public long getNanos() { return nanos; }

    public static String csvHeader() {
        return "algo,distribution,n,trial,time_nanos,comparisons,swaps,reads,writes";
    }

    public String toCsvRow(String algo, String distribution, int n, int trial) {
        return String.format("%s,%s,%d,%d,%d,%d,%d,%d,%d",
                algo, distribution, n, trial,
                nanos, comparisons, swaps, arrayReads, arrayWrites);
    }
}
