package etu.nic.git;


import etu.nic.git.trajectories_swing.tools.TrajectoryDataRow;
import org.junit.Assert;
import org.junit.Test;

public class TrajectoryDataRowTest {

    @Test
    public void trajectoryStringShouldBeInvalid_separator() {
        String testString = "2.000, 1976091.0  3974537.2  4564998.2  1.671  4.251  4.931";
        Assert.assertFalse(TrajectoryDataRow.isValidTrajectoryString(testString));
    }
    @Test
    public void trajectoryStringShouldBeInvalid_amountOfParameters_less() {
        String testString = "2.000, 1976091.0  3974537.2  4564998.2  1.671  4.251";
        Assert.assertFalse(TrajectoryDataRow.isValidTrajectoryString(testString));
    }
    @Test
    public void trajectoryStringShouldBeInvalid_amountOfParameters_more() {
        String testString = "2.000, 1976091.0  3974537.2  4564998.2  1.671  4.251 4.931 4.931";
        Assert.assertFalse(TrajectoryDataRow.isValidTrajectoryString(testString));
    }
    @Test
    public void trajectoryStringShouldBeValid() {
        String testString = "2.000  1976091.0  3974537.2  4564998.2  1.671  4.251  4.931";
        Assert.assertTrue(TrajectoryDataRow.isValidTrajectoryString(testString));
    }

    @Test
    public void indexOfInvalidParameterShouldEqualNegativeOne() {
        String testString = "2.000  1976091.0  3974537.2  4564998.2  1.671  4.251  4.931";
        Assert.assertEquals(-1, TrajectoryDataRow.indexOfInvalidParameter(testString));
    }
    @Test
    public void indexOfInvalidParameterShouldEqualZero() {
        String testString = "2.000,  1976091.0  3974537.2  4564998.2  1.671  4.251  4.931";
        Assert.assertEquals(0, TrajectoryDataRow.indexOfInvalidParameter(testString));
    }
    @Test
    public void indexOfInvalidParameterShouldEqualSix() {
        String testString = "2.000 1976091.0  3974537.2  4564998.2  1.671  4.251  4.931;";
        Assert.assertEquals(6, TrajectoryDataRow.indexOfInvalidParameter(testString));
    }
}
