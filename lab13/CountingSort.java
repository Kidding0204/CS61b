import java.util.Arrays;

/**
 * Class with 2 ways of doing Counting sort, one naive way and one "better" way
 *
 * @author Akhil Batra, Alexander Hwang
 *
 **/
public class CountingSort {
    /**
     * Counting sort on the given int array. Returns a sorted version of the array.
     * Does not touch original array (non-destructive method).
     * DISCLAIMER: this method does not always work, find a case where it fails
     *
     * @param arr int array that will be sorted
     * @return the sorted array
     */
    public static int[] naiveCountingSort(int[] arr) {
        // find max
        int max = Integer.MIN_VALUE;
        for (int i : arr) {
            max = max > i ? max : i;
        }

        // gather all the counts for each value
        int[] counts = new int[max + 1];
        for (int i : arr) {
            counts[i]++;
        }

        // when we're dealing with ints, we can just put each value
        // count number of times into the new array
        int[] sorted = new int[arr.length];
        int k = 0;
        for (int i = 0; i < counts.length; i += 1) {
            for (int j = 0; j < counts[i]; j += 1, k += 1) {
                sorted[k] = i;
            }
        }

        // however, below is a more proper, generalized implementation of
        // counting sort that uses start position calculation
        int[] starts = new int[max + 1];
        int pos = 0;
        for (int i = 0; i < starts.length; i += 1) {
            starts[i] = pos;
            pos += counts[i];
        }

        int[] sorted2 = new int[arr.length];
        for (int i = 0; i < arr.length; i += 1) {
            int item = arr[i];
            int place = starts[item];
            sorted2[place] = item;
            starts[item] += 1;
        }

        // return the sorted array
        return sorted;
    }

    /**
     * Counting sort on the given int array, must work even with negative numbers.
     * Note, this code does not need to work for ranges of numbers greater
     * than 2 billion.
     * Does not touch original array (non-destructive method).
     *
     * @param arr int array that will be sorted
     */
    public static int[] betterCountingSort(int[] arr) {
        // TODO make counting sort work with arrays containing negative numbers.
        int[] lenAndPosMin = findCountLenAndMin(arr);
        int len = lenAndPosMin[0];
        int negMin = lenAndPosMin[1];
        int[] counts = new int[len];
        assignCounts(counts, arr, negMin);

        return countingSort(counts, arr.length);
    }
    private static int[] findCountLenAndMin(int[] arr) {
        int posMax = 0;
        int negMin = 0;
        for (int num : arr) {
            if (num > 0) {
                posMax = Math.max(posMax, num);
            } else if (num < 0) {
                negMin = Math.min(negMin, num);
            }
        }
        int len = posMax - negMin + 1;
        return new int[]{len, negMin};
    }
    private static void assignCounts(int[] counts, int[] arr, int negMin) {
        for (int num : arr) {
            int index = num - negMin;
            counts[index] += 1;
        }
    }
    private static int[] countingSort(int[] counts, int arrLen) {
        int[] sortedArr = new int[arrLen];
        int j = 0;
        for (int i = 0; i < counts.length; i++) {
            int count = counts[i];
            while(count != 0) {
                sortedArr[j] = i;
                j++;
                count--;
            }
        }
        return sortedArr;
    }

    public static void main(String[] args) {
        int[] test = {9, 8, 7 ,4 ,5 ,6, 2};
        int[] sortedTest = betterCountingSort(test);
        System.out.println(Arrays.toString(test));
        System.out.println(Arrays.toString(sortedTest));
    }
}
