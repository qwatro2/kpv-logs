package backend.academy.logs.percentiles;

import com.tdunning.math.stats.TDigest;

public class TDigestPercentileCounter implements PercentileCounter {
    private static final double COMPRESSION = 100.0;

    private TDigest tDigest;
    private double targetPercentile;

    @Override
    public void reset(double targetPercentile) {
        this.tDigest = TDigest.createDigest(COMPRESSION);
        this.targetPercentile = targetPercentile;
    }

    @Override
    public void processNext(int value) {
        tDigest.add(value);
    }

    @Override
    public double getPercentile() {
        return tDigest.quantile(targetPercentile);
    }
}
