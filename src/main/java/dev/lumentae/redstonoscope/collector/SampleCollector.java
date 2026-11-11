package dev.lumentae.redstonoscope.collector;

public class SampleCollector {
    private int sampleSize = 200;
    private int[] samples = new int[sampleSize];
    private int index = 0;

    public void addSample(int sample) {
        samples[index] = sample;
        index = (index + 1) % sampleSize;
    }

    public int[] getLastSamples() {
        return getLastNSamples(sampleSize);
    }

    public int[] getLastNSamples(int n) {
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = samples[(index - i - 1 + sampleSize) % sampleSize];
        }
        return result;
    }

    public int getSampleSize() {
        return sampleSize;
    }

    public void setSampleSize(int sampleSize) {
        this.sampleSize = sampleSize;
        this.samples = new int[sampleSize];
    }
}
