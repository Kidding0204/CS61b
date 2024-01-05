package lab14;

import lab14lib.*;

import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		Generator g1 = new SineWaveGenerator(60);
		Generator g2 = new SineWaveGenerator(61);

		ArrayList<Generator> generators = new ArrayList<>();
		generators.add(g1);
		generators.add(g2);
		MultiGenerator mg = new MultiGenerator(generators);

		GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(mg);
		gav.drawAndPlay(500000, 1000000);
	}
} 