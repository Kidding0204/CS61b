import static org.junit.Assert.*;
import org.junit.Test;
public class TestArrayDequeGold {
    @Test
    public void TAD() {
        ArrayDequeSolution<Integer> expected = new ArrayDequeSolution<>();
        StudentArrayDeque<Integer> actual = new StudentArrayDeque<>();
        String log = "";

        for (int i = 0; i < 1000; i++) {
            int x = StdRandom.uniform(4);
            int addNumber = StdRandom.uniform(1000);
            if (actual.isEmpty()) {
                x = StdRandom.uniform(2);
            }

            switch(x) {
                case 0:
                    log = log + "addFirst(" + addNumber + ")\n";
                    expected.addFirst(addNumber);
                    actual.addFirst(addNumber);
                    assertEquals(log, expected.getFirst(), actual.get(0));
                    break;
                case 1:
                    log = log + "addLast(" + addNumber + ")\n";
                    expected.addLast(addNumber);
                    actual.addLast(addNumber);
                    assertEquals(log, expected.getLast(), actual.get(actual.size() - 1));
                    break;
                case 2:
                    log = log + "removeFirst()\n";
                    assertEquals(log, expected.removeFirst(), actual.removeFirst());
                    break;
                case 3:
                    log = log + "removeLast()\n";
                    assertEquals(log, expected.removeLast(), actual.removeLast());
                    break;
                default:
            }
        }
    }
}
