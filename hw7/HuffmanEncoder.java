import java.util.HashMap;
import java.util.Map;

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

    }
}
