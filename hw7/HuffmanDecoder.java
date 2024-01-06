import java.util.LinkedList;
import java.util.List;

public class HuffmanDecoder {
    private static List<Character> streamConvertToChars(BitSequence stream, BinaryTrie trie) {
        List<Character> charList = new LinkedList<>();
        if (stream.length() == 0) {
            return null;
        }
        Match curr = trie.longestPrefixMatch(stream);
        int len = curr.getSequence().length();
        charList.add(curr.getSymbol());
        List<Character> currList = streamConvertToChars(stream.allButFirstNBits(len), trie);
        if (currList != null) {
            charList.add(currList.removeFirst());
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
        assert chars != null;
        FileUtils.writeCharArray(args[1], chars);
    }
}
