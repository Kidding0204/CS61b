package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private final Percolation[] samples;
    private final int grid;
    private final int num;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        grid = N;
        num = T;
        samples = new Percolation[T];
        for (int i = 0; i < T; i++) {
            samples[i] = pf.make(N);
        }
    }
    private double computeThreshold(Percolation sample) {
        if (sample.percolates()) {
            return (double) sample.numberOfOpenSites() / (grid * grid);
        }

        int row = StdRandom.uniform(grid);
        int column = StdRandom.uniform(grid);
        sample.open(row, column);
        return computeThreshold(sample);
    }

    private double[] getThresholds() {
        double[] thresholds = new double[num];
        for (int i = 0; i < num; i++) {
            thresholds[i] = computeThreshold(samples[i]);
        }
        return thresholds;
    }
    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(getThresholds());
    }
    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(getThresholds());
    }
    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(num);
    }
    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(num);
    }
}
