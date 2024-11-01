package backend.academy.logs.percentiles;

public interface PercentileCounter {
    void reset(double targetPercentile);

    void processNext(int value);

    double getPercentile();
}
