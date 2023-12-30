import java.util.Arrays;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        // TODO: Implement LSD Sort
        String[] strings = new String[asciis.length];
        System.arraycopy(asciis, 0, strings, 0, asciis.length);

        int maxLen = 0;
        for (String ascii : asciis) {
            int len = ascii.length();
            maxLen = Math.max(maxLen, len);
        }

        while (maxLen > 0) {
            sortHelperLSD(strings, maxLen - 1);
            maxLen--;
        }

        return strings;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        int[] counts = new int[257];
        assignCounts(counts, asciis, index);
        int[] ends = endIndices(counts);
        asciis = LSDSort(asciis, ends, index);
    }
    private static void assignCounts(int[] counts, String[] asciis, int index) {
        for (String curr : asciis) {
            if (curr.length() - 1 < index) {
                counts[0] += 1;
                continue;
            }
            char checking = curr.charAt(index);
            counts[checking + 1] += 1;
        }
    }
    private static int[] endIndices(int[] counts) {
        int[] ends = new int[counts.length];
        ends[0] = counts[0];
        for (int i = 1; i < counts.length; i++) {
            ends[i] = ends[i - 1] + counts[i];
        }
        return ends;
    }
    private static String[] LSDSort(String[] asciis, int[] ends, int index) {
        String[] sorted = new String[asciis.length];
        for (int i = asciis.length - 1; i > -1; i--) {
            String ascii = asciis[i];
            int idxOfEnds = 0;
            if (ascii.length() - 1 >= index) {
                idxOfEnds = ascii.charAt(index) + 1;
            }
            if (idxOfEnds != 0 && ends[idxOfEnds] == ends[idxOfEnds - 1]) {
                continue;
            }
            ends[idxOfEnds]--;
            int idxOfSorted = ends[idxOfEnds];
            sorted[idxOfSorted] = ascii;
        }
        return sorted;
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }

    public static void main(String[] args) {
        String[] strings = {"asuka", "apple", "orange", "banana", "king", "zap"};
        String[] sorted = sort(strings);
        System.out.println(Arrays.toString(strings));
        System.out.println(Arrays.toString(sorted));
    }
}
