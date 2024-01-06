import java.util.*;

public class HuffmanEncoder {
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> freqTable = new HashMap<>();

        for (char curr : inputSymbols) {
            if (freqTable.containsKey(curr)) {
                int count = freqTable.get(curr);
                freqTable.put(curr, count + 1);
            } else {
                freqTable.put(curr, 1);
            }
        }

        return freqTable;
    }
    public static void main(String[] args) {
        char[] file8Bit = FileUtils.readFile(args[0]);
        Map<Character, Integer> freqTable = buildFrequencyTable(file8Bit);
        BinaryTrie trie = new BinaryTrie(freqTable);
        ObjectWriter ow = new ObjectWriter(STR."\{args[0]}.huf");
        ow.writeObject(trie);
        Map<Character, BitSequence> table = trie.buildLookupTable();
        List<BitSequence> bitSequences = new ArrayList<>();
        for (char character : file8Bit) {
            bitSequences.add(table.get(character));
        }
        BitSequence all = BitSequence.assemble(bitSequences);
        ow.writeObject(all);
    }
}
