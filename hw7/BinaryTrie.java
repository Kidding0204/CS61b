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
    private int longestPrefixNum(BitSequence querySequence, Node pre) {
        int index = querySequence.bitAt(0);
        Node curr = pre.children[index];
        if (curr == null) {
            return 0;
        }
        BitSequence lastSeq = querySequence.lastNBits(querySequence.length() - 1);
        int count = longestPrefixNum(lastSeq, curr);
        return count + 1;
    }
    public Match longestPrefixMatch(BitSequence querySequence) {
        int prefixNum = longestPrefixNum(querySequence, root);
        BitSequence prefix = querySequence.firstNBits(prefixNum);

        Node end = root;
        for (int i = 0; i < prefixNum; i++) {
            int index = prefix.bitAt(i);
            end = end.children[index];
        }

        return new Match(prefix, end.item);
    }
    public Map<Character, BitSequence> buildLookupTable() {
        return null;
    }
}