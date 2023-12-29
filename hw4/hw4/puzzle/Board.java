package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

import java.util.Random;

public class Board implements WorldState {
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
        this.tiles = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(tiles[i], 0, this.tiles[i], 0, size);
        }
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
            throw new IndexOutOfBoundsException();
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
                int xCorrect = correct.x;
                int yCorrect = correct.y;
                sum += Math.abs(i - xCorrect) + Math.abs(j - yCorrect);
            }
        }
        return sum;
    }
    @Override
    public int hashCode() {
        Random r = new Random(0);
        int hashCode = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int curr = tiles[i][j];
                int fraction = r.nextInt();
                hashCode = fraction * (curr + i + j);
            }
        }
        return hashCode;
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
        if (o.size != this.size) {
            return false;
        }
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
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }
    private Pos correctPos(int n) {
        int xCorrect = n / size;
        int yCorrect = n % size - 1;
        if (yCorrect == -1) {
            xCorrect--;
            yCorrect = size - 1;
        }
        return new Pos(xCorrect, yCorrect);
    }
}
