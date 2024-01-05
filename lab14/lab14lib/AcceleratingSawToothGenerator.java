package lab14lib;

import lab14.SineWaveGenerator;

public class AcceleratingSawToothGenerator implements Generator {
    private int state;
    private int period;
    private int count;
    private final double factor;

    public AcceleratingSawToothGenerator(int period, double factor) {
        this.period = period;
        this.factor = factor;
        state = 0;
        count = 0;
    }
    private double normalize(int x) {
        int frequency = x % period;
        double result =  (double) (frequency - period / 2) / period * 2;
        count += 1;
        if (count == period) {
            count = 0;
            period = (int) (period * factor);
            state = period;
        }
        return result;
    }

    public double next() {
        state += 1;
        return normalize(state);
    }
}
