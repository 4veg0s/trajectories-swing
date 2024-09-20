package etu.nic.git.trajectories_swing.tool;

import org.junit.Assert;
import org.junit.Test;

public class StatisticsToolTest {
    private static final double DELTA = 0.00000001;

    @Test
    public void averageOfArrayCountable() {
        double[] array = new double[]{1, 2, 3, 4, 5};
        Assert.assertEquals(3, StatisticsTool.getAverageOfArray(array), DELTA);
    }

    @Test
    public void dispersionOfArrayCountable() {
        double[] array = new double[]{1, 2, 3, 4, 5};
        Assert.assertEquals(2, StatisticsTool.getDispersionOfArray(array), DELTA);
    }

    @Test
    public void secondMomentOfArrayCountable() {
        double[] array = new double[]{4, 8, 6, 5, 3};
        Assert.assertEquals(3.7, StatisticsTool.getSecondMomentOfArray(array), DELTA);
    }

    @Test
    public void thirdMomentOfArrayCountable() {
        double[] array = new double[]{4, 8, 6, 5, 3};
        Assert.assertEquals(2.016, StatisticsTool.getThirdMomentOfArray(array), DELTA);
    }

    @Test
    public void secondMomentOfArrayIsNaN() {
        double[] array = new double[]{1};
        Assert.assertTrue(Double.isNaN(StatisticsTool.getSecondMomentOfArray(array)));
    }
}
