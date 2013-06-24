package pl.edu.agh.recommendationsystems.algorithms.sbc;

/**
 * Created with IntelliJ IDEA.
 * User: Łukasz Macionczyk
 * Date: 24.06.13
 * Time: 00:25
 * To change this template use File | Settings | File Templates.
 */
public class DistanceCalculator {

    public static enum Metrics {
        MANHATTAN, EUCLIDEAN, CHEBYSHEV
    }

    /**
     * Calculates distance between 2 points in N-dimensional space using default (Euclidean) metrics

     * @return distance between points
     */
    public static double getDistance(int[] point1, int[] point2) {
        return getDistance(point1, point2, Metrics.EUCLIDEAN);
    }

    /**
     * Calculates distance between 2 points in N-dimensional space using given metrics

     * @return distance between points
     */
    public static double getDistance(int[] point1, int[] point2, Metrics metrics) {
        if (point1.length != point2.length)
            throw new IllegalArgumentException("point1.length != point2.length");

        int[] detailDistances = new int[point1.length];
        for (int i = 0; i < point1.length; ++i) {
            detailDistances[i] = Math.abs(point1[i] - point2[i]);
        }

        if (metrics == Metrics.MANHATTAN) {
            return calculateManhattan(detailDistances);
        }

        if (metrics == Metrics.EUCLIDEAN) {
            return calculateEuclidean(detailDistances);
        }

        if (metrics == Metrics.CHEBYSHEV) {
            return calculateChebyshev(detailDistances);
        }

        throw new IllegalArgumentException("unknown metrics");
    }

    private static double calculateManhattan(int[] values) {
        int sum = 0;
        for (int v : values) {
            sum += v;
        }
        return sum;
    }

    private static double calculateEuclidean(int[] values) {
        int sum = 0;
        for (int v : values) {
            sum += v * v;
        }
        return Math.sqrt(sum);
    }

    private static double calculateChebyshev(int[] values) {
        int max = 0;
        for (int v : values) {
            if (v > max) {
                max = v;
            }
        }
        return max;
    }

    private DistanceCalculator() {}
}
