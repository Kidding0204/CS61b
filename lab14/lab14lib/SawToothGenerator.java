package lab14lib;

public class SawToothGenerator implements Generator {
    int period;
    int state;
    SawToothGenerator(int period) {
        this.period = period;
        state = 0;
    }
    private double normalize(int x) {
        int frequency = x % period;
        return (double) (frequency - period / 2) / period * 2;
    }
    public double next() {
        state += 1;
        return normalize(state);
    }
}