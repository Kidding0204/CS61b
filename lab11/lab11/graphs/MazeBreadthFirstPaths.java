package lab11.graphs;

import java.util.LinkedList;
import java.util.Queue;
/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;
    private Queue<Integer> fringe;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
        fringe = new LinkedList<>();
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs(int v) {
        // TODO: Your code here. Don't forget to update distTo, edgeTo, and marked, as well as call announce()
        fringe.add(v);

        while (!fringe.isEmpty()) {
            v = fringe.remove();
            marked[v] = true;
            announce();
            for (int w : maze.adj(v)) {
                if (!marked[w]) {
                    fringe.add(w);
                    edgeTo[w] = v;
                    announce();
                    distTo[w] = distTo[v] + 1;
                }
            }
            if (v == t) {
                targetFound = true;
                return;
            }
        }
    }


    @Override
    public void solve() {
        bfs(s);
    }
}

