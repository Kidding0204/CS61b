package lab14lib;

public class StrangeBitwiseGenerator implements Generator {
    private final int period;
    private int state;
    public StrangeBitwiseGenerator(int period) {
        this.period = period;
        state = 0;
    }
    private double normalize(int x) {
        int frequency = x % period;
        return (double) (frequency - period / 2) / period * 2;
    }
    public double next() {
        state += 1;
        int weirdState = state & (state >> 3) & (state >> 8) % period;
        return normalize(weirdState);
    }
}
