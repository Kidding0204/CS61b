import java.util.LinkedList;
import java.util.List;

public class HuffmanDecoder {
    private static List<Character> streamConvertToChars(BitSequence stream, BinaryTrie trie) {
        List<Character> charList = new LinkedList<>();

        while (stream.length() != 0) {
            Match curr = trie.longestPrefixMatch(stream);
            charList.add(curr.getSymbol());
            int len = curr.getSequence().length();
            stream = stream.allButFirstNBits(len);
        }

        return charList;
    }
    public static void main(String[] args) {
        ObjectReader hufFile = new ObjectReader(args[0]);
        Object trieO = hufFile.readObject();
        Object streamO = hufFile.readObject();
        BinaryTrie trie = (BinaryTrie) trieO;
        BitSequence stream = (BitSequence) streamO;

        List<Character> chars = streamConvertToChars(stream, trie);
        char[] arr = new char[chars.size()];
        for (int i = 0; i < chars.size(); i++) {
            arr[i] = chars.remove(0);
        }
        FileUtils.writeCharArray(args[1], arr);
    }
}
