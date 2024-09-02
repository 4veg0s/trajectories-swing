package etu.nic.git.trajectories_swing.tools;

import org.junit.Assert;
import org.junit.Test;

public class StatisticsToolTest {
    private static final double DELTA = 0.00000001;

    @Test
    public void getAverageOfArrayCorrect() {
        double[] array = new double[]{1, 2, 3, 4, 5};
        Assert.assertEquals(3, StatisticsTool.getAverageOfArray(array), DELTA);
    }

    @Test
    public void getDispersionOfArrayCorrect() {
        double[] array = new double[]{1, 2, 3, 4, 5};
        Assert.assertEquals(2, StatisticsTool.getDispersionOfArray(array), DELTA);
    }

    @Test
    public void getSecondMomentOfArrayCorrect() {
        double[] array = new double[]{4, 8, 6, 5, 3};
        Assert.assertEquals(3.7, StatisticsTool.getSecondMomentOfArray(array), DELTA);
    }

    @Test
    public void getThirdMomentOfArrayCorrect() {
        double[] array = new double[]{4, 8, 6, 5, 3};
        Assert.assertEquals(2.016, StatisticsTool.getThirdMomentOfArray(array), DELTA);
    }
}
