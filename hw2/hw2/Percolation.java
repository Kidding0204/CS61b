package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    boolean[][] sites;
    boolean[][] waters;
    int openNum;
    WeightedQuickUnionUF sets;
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N can't equal or smaller than 0!");
        }
        sites = new boolean[N][N];
        waters = new boolean[N][N];
        for (boolean top : waters[N-1]) {
            top = true;
        }
        openNum = 0;
        sets = new WeightedQuickUnionUF(N * N);
    }

    public void open(int row, int col) {
        sites[row][col] = true;
        openNum++;
    }
    public boolean isOpen(int row, int col) {
        return sites[row][col];
    }
    public int numberOfOpenSites() {
        return openNum;
    }
    public static void main(String[] args) {

    }
}