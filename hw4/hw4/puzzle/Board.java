package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState{
    private static final int BLANK = 0;
    private final int[][] tiles;
    private final int size;
    private static class Pos {
        int x;
        int y;
        Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    public Board(int[][] tiles) {
        size = tiles.length;
        this.tiles = tiles;
    }

    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }
    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = BLANK;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = BLANK;
                }
            }
        }
        return neighbors;
    }

    public int tileAt(int i, int j) {
        if (i < 0 || i >= size || j < 0 || j > size) {
            throw new IllegalArgumentException("Array Bound Exception");
        }
        return tiles[i][j];
    }
    public int size() {
        return size;
    }
    public int hamming() {
        int sum = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int tile = tiles[i][j];
                if (tile == BLANK) {
                    continue;
                }
                Pos correct = correctPos(tile);
                if (i != correct.x || j != correct.y) {
                    sum++;
                }
            }
        }
        return sum;
    }
    public int manhattan() {
        int sum = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int tile = tiles[i][j];
                if (tile == BLANK) {
                    continue;
                }
                Pos correct = correctPos(tile);
                sum += Math.abs(i - correct.x) + Math.abs(j - correct.y);
            }
        }
        return sum;
    }
    @Override
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (y == this) {
            return true;
        }
        if (y.getClass() != this.getClass()) {
            return false;
        }

        Board o = (Board) y;
        boolean equal = true;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int tile = tiles[i][j];
                if (o.tiles[i][j] != tile) {
                    equal = false;
                    break;
                }
            }
        }
        return equal;
    }

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N).append("\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }
    private Pos correctPos(int n) {
        int xCorrect = n / size;
        int yCorrect = n % size;
        return new Pos(xCorrect, yCorrect);
    }
    public static void main(String[] args) {
        int[][] tiles = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        Board t = new Board(tiles);
        String test = t.toString();
        System.out.println(test);
    }
}
