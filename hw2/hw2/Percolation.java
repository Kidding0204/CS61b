package hw2;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.LinkedList;
import java.util.List;

public class Percolation {
    private final boolean[][] sites;
    private final boolean[][] watered;
    private int openNum;
    private final int grid;
    private final WeightedQuickUnionUF sets;
    public Percolation(int N) {
        grid = N;
        if (N <= 0) {
            throw new IllegalArgumentException("N can't equal or smaller than 0!");
        }
        sites = new boolean[N][N];
        watered = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            watered[0][i] = true;
        }
        openNum = 0;
        sets = new WeightedQuickUnionUF(N * N);
    }

    public void open(int row, int col) {
        if (isOpen(row, col)) {
            return;
        }
        sites[row][col] = true;
        openNum++;
        List<Integer> openedAdjacentSites = getConnectAdjacent(row, col);
        unionSets(openedAdjacentSites);
    }
    private void unionSets(List<Integer> nodes) {
        int origin = nodes.remove(0);
        int size = nodes.size();
        for (int i = 0; i < size; i++) {
            int site = nodes.remove(0);
            if (isFull(site) || isFull(origin)) {
                sets.union(origin, site);
                changeWatered(origin);
            } else {
                sets.union(origin, site);
            }
        }
    }
    private void changeWatered(int index) {
        int rootIndex = sets.find(index);
        int rootRow = rootIndex / grid;
        int rootCol = rootIndex % grid;
        watered[rootRow][rootCol] = true;
    }
    private List<Integer> getConnectAdjacent(int row, int col) {
        int oIndex = row * grid + col;
        int aIndex;
        List<Integer> openedAdjacent = new LinkedList<>();
        openedAdjacent.add(oIndex);

        if (isOpen(row - 1, col)) {
            aIndex = oIndex - grid;
            if (!sets.connected(oIndex, aIndex)) {
                openedAdjacent.add(aIndex);
            }
        }
        if (isOpen(row + 1, col)) {
            aIndex = oIndex + grid;
            if (!sets.connected(oIndex, aIndex)) {
                openedAdjacent.add(aIndex);
            }
        }
        if (isOpen(row, col - 1)) {
            aIndex = oIndex - 1;
            if (!sets.connected(oIndex, aIndex)) {
                openedAdjacent.add(aIndex);
            }
        }
        if (isOpen(row, col + 1)) {
            aIndex = oIndex + 1;
            if (!sets.connected(oIndex, aIndex)) {
                openedAdjacent.add(aIndex);
            }
        }
        return openedAdjacent;
    }
    public boolean isOpen(int row, int col) {
        if (row >= grid || col >= grid || row < 0 || col < 0) {
            return false;
        }
        return sites[row][col];
    }
    public boolean isFull(int row, int col) {
        int rootIndex = sets.find(row * grid + col);
        int rootRow = rootIndex / grid;
        int rootCol = rootIndex % grid;
        return isOpen(row, col) && watered[rootRow][rootCol];
    }
    private boolean isFull(int index) {
        int rootIndex = sets.find(index);
        int rootRow = rootIndex / grid;
        int rootCol = rootIndex % grid;
        return watered[rootRow][rootCol];
    }
    public int numberOfOpenSites() {
        return openNum;
    }
    public boolean percolates() {
        boolean r = false;
        for (int i = 0; i < grid; i++) {
            int index = grid * (grid - 1) + i;
            if (sites[grid - 1][i]) {
                if (isFull(index)) {
                    r = true;
                    break;
                }
            }
        }
        return r;
    }
    public static void main(String[] args) {

    }
}
