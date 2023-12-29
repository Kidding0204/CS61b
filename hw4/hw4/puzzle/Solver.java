package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.LinkedList;
import java.util.List;

public class Solver {
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
        return BestFirstSearch().moves;
    }
    public Iterable<WorldState> solution() {
        Node goal = BestFirstSearch();
        List<WorldState> queue = new LinkedList<>();
        getQueue(queue, goal);
        return queue;
    }

    private Node BestFirstSearch() {
        Node checkingNode = fringe.delMin();
        while (!checkingNode.state.isGoal()) {
            checkingNode = visit(checkingNode);
        }
        return checkingNode;
    }
    private Node visit(Node curr) {
        for (WorldState neighbor : curr.state.neighbors()) {
            if (neighbor.equals(curr.state)) {
                continue;
            }
            int neighborMoves = curr.moves + 1;
            Node neighborNode = new Node(neighbor, curr, neighborMoves);
            fringe.insert(neighborNode);
        }

        return fringe.delMin();
    }
    private Node getQueue(List<WorldState> queue, Node goal) {
        if (goal.pre == null) {
            return goal;
        }
        queue.add(getQueue(queue, goal.pre).state);
        return goal;
    }
}
