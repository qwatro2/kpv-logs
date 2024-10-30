package backend.academy.logs.percentiles;

import com.tdunning.math.stats.TDigest;

public class TDigestPercentileCounter implements PercentileCounter {
    private TDigest tDigest;
    private double targetPercentile;

    @Override
    public void reset(double targetPercentile) {
        this.tDigest = TDigest.createDigest(100.0);
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
