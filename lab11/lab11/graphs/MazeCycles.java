package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private boolean cycleFound = false;
    private int cross = -1;
    private Maze maze;

    public MazeCycles(Maze m) {
        super(m);
        maze = m;
    }

    @Override
    public void solve() {
        // TODO: Your code here!
        cycleFound(0, 0);
    }

    // Helper methods go here
    private void cycleFound(int v, int pre) {
        marked[v] = true;
        announce();

        for (int w : maze.adj(v)) {
            if (w == pre) {
                continue;
            }
            if (marked[w]) {
                cross = w;
                cycleFound = true;
                edgeTo[w] = v;
                return;
            } else {
                cycleFound(w, v);
                edgeTo[w] = v;
                break;
            }
        }

        if (v == cross) {
            announce();
        }
    }
}

