package hw4.puzzle;

public class Board implements WorldState{
    public Board(int[][] tiles) {

    }

    @Override
    public int estimatedDistanceToGoal() {
        return 0;
    }
    @Override
    public Iterable<WorldState> neighbors() {
        return null;
    }

    public int tileAt(int i, int j) {
        return 0;
    }
    public int size() {
        return 0;
    }
    public int hamming() {
        return 0;
    }
    public int manhattan() {
        return 0;
    }
    @Override
    public boolean equals(Object y) {
        return false;
    }

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }
}
