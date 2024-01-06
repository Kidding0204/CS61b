import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.PriorityQueue;

public class BinaryTrie implements Serializable {
    private Node root;
    private static class Node implements Comparable<Node> {
        char item;
        int frequency;
        Node[] children;
        Node(char item, int frequency) {
            this.item = item;
            this.frequency = frequency;
            children = new Node[2];
        }

        @Override
        public int compareTo(Node o) {
            return this.frequency - o.frequency;
        }
    }

    private Node[] establishNodes(Map<Character, Integer> frequencyTable) {
        Node[] nodes = new Node[frequencyTable.size()];

        int i = 0;
        for (Character key : frequencyTable.keySet()) {
            int freq = frequencyTable.get(key);
            nodes[i] = new Node(key, freq);
            i++;
        }

        return nodes;
    }
    private Node[] findTwoSmallestNodes(PriorityQueue<Node> pq) {
        Node[] twoSmallest = new Node[2];

        for (int i = 0; i < 2; i++) {
            twoSmallest[i] = pq.remove();
        }

        return twoSmallest;
    }
    private void buildTrie(PriorityQueue<Node> pq) {
        if (pq.size() == 1) {
            root = pq.remove();
            return;
        }
        Node[] twoSmallest = findTwoSmallestNodes(pq);
        int freq = twoSmallest[0].frequency + twoSmallest[1].frequency;
        Node parent = new Node('\0', freq);
        System.arraycopy(twoSmallest, 0, parent.children, 0, 2);
        pq.add(parent);
        buildTrie(pq);
    }
    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        Node[] nodes = establishNodes(frequencyTable);

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.addAll(Arrays.asList(nodes));

        buildTrie(pq);
    }
    public Match longestPrefixMatch(BitSequence querySequence) {
        return null;
    }
    public Map<Character, BitSequence> buildLookupTable() {
        return null;
    }
}
