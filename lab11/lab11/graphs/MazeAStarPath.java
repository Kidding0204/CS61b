package lab11.graphs;

import edu.princeton.cs.algs4.In;

import java.util.*;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;
    private PriorityQueue<Node> fringe = new PriorityQueue<>(Node::compareTo);

    private class Node implements Comparable<Node> {
        int w;
        int priority;

        Node(int v) {
            this.w = v;
            this.priority = distTo[v];
        }

        @Override
        public int compareTo(Node o) {
            return priority - o.priority;
        }
    }

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        int sourceX = maze.toX(v);
        int sourceY = maze.toY(v);
        int targetX = maze.toX(t);
        int targetY = maze.toY(t);
        return Math.abs(sourceX - targetX) + Math.abs(sourceY - targetY);
    }

    /** Finds vertex estimated to be closest to target. */
    private int relax(int v) {
        for (int w : maze.adj(v)) {
            if (marked[w]) {
                continue;
            }
            distTo[w] = distTo[v] + 1;
            Node curr = new Node(w);
            curr.priority = h(w) + distTo[w];
            fringe.add(curr);
            edgeTo[w] = v;
            announce();
        }
        return fringe.remove().w;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {

        Node curr = new Node(s);
        fringe.add(curr);

        while (!fringe.isEmpty()) {
            s = relax(s);
            marked[s] = true;
            announce();
            if (s == t) {
                break;
            }
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

}

