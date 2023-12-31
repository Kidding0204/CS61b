package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.LinkedList;
import java.util.List;

public class Solver {
    private Node goal;
    private final MinPQ<Node> fringe;
    private static class Node implements Comparable<Node> {
        WorldState state;
        int moves;
        int estimateDist;
        Node pre;
        Node(WorldState state, Node pre, int moves) {
            this.state = state;
            this.pre = pre;
            this.moves = moves;
            estimateDist = state.estimatedDistanceToGoal();
        }
        @Override
        public int compareTo(Node o) {
            return moves + estimateDist - (o.moves + o.estimateDist);
        }
    }
    public Solver(WorldState initial) {
        if (initial == null) {
            throw new IllegalArgumentException("initial can't be null");
        }
        Node i = new Node(initial, null, 0);
        fringe = new MinPQ<>(Node::compareTo);
        fringe.insert(i);
    }

    public int moves() {
        bestFirstSearch();
        return goal.moves;
    }
    public Iterable<WorldState> solution() {
        bestFirstSearch();
        List<WorldState> queue = new LinkedList<>();
        getQueue(queue, goal);
        return queue;
    }

    private void bestFirstSearch() {
        if (goal != null) {
            return;
        }
        while (!fringe.isEmpty()) {
            Node checkingNode = fringe.delMin();
            if (checkingNode.state.isGoal()) {
                goal = checkingNode;
                break;
            }
            visit(checkingNode);
        }
    }
    private void visit(Node curr) {
        for (WorldState neighbor : curr.state.neighbors()) {
            if (curr.pre != null) {
                if (neighbor.equals(curr.pre.state)) {
                    continue;
                }
            }
            int neighborMoves = curr.moves + 1;
            Node neighborNode = new Node(neighbor, curr, neighborMoves);
            fringe.insert(neighborNode);
        }
    }
    private void getQueue(List<WorldState> queue, Node g) {
        if (g == null) {
            return;
        }
        getQueue(queue, g.pre);
        queue.add(g.state);
    }
}
