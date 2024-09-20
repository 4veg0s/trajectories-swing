package etu.nic.git.trajectories_swing.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TrajectoryRowTableModelTest {
    TrajectoryRowTableModel model;
    List<TrajectoryRow> list;

    @Before
    public void setUp() throws Exception {
        list = new ArrayList<>();
        list.add(new TrajectoryRow(1, 2, 3, 4, 5, 6, 7));
        list.add(new TrajectoryRow(2, 2, 3, 4, 5, 6, 7));
        list.add(new TrajectoryRow(3, 2, 3, 4, 5, 6, 7));
        model = new TrajectoryRowTableModel(list);
    }

    @Test
    public void insertRowAboveFirstRow() {
        int index = 0;
        List<TrajectoryRow> listWithNewRow = new ArrayList<>(list);
        TrajectoryRow emptyRow = new TrajectoryRow(1, 0, 0, 0, 0, 0, 0);
        listWithNewRow.add(index, emptyRow);
        model.insertRowAbove(index);
        List<TrajectoryRow> modelList = model.getTrajectoryRowList();
        Assert.assertEquals(listWithNewRow, modelList);
    }

    @Test
    public void insertRowAboveSecondRow() {
        int index = 1;
        List<TrajectoryRow> listWithNewRow = new ArrayList<>(list);
        TrajectoryRow emptyRow = new TrajectoryRow(1.5, 0, 0, 0, 0, 0, 0);
        listWithNewRow.add(index, emptyRow);
        model.insertRowAbove(index);
        List<TrajectoryRow> modelList = model.getTrajectoryRowList();
        Assert.assertEquals(listWithNewRow, modelList);
    }

    @Test
    public void insertRowBelowLastRow() {
        int index = list.size() - 1;
        List<TrajectoryRow> listWithNewRow = new ArrayList<>(list);
        TrajectoryRow emptyRow = new TrajectoryRow(3, 0, 0, 0, 0, 0, 0);
        listWithNewRow.add(emptyRow);
        model.insertRowBelow(index);
        List<TrajectoryRow> modelList = model.getTrajectoryRowList();
        Assert.assertEquals(listWithNewRow, modelList);
    }

    @Test
    public void insertRowBelowPreLastRow() {
        int index = list.size() - 2;
        List<TrajectoryRow> listWithNewRow = new ArrayList<>(list);
        TrajectoryRow emptyRow = new TrajectoryRow(2.5, 0, 0, 0, 0, 0, 0);
        listWithNewRow.add(index + 1, emptyRow);
        model.insertRowBelow(index);
        List<TrajectoryRow> modelList = model.getTrajectoryRowList();
        Assert.assertEquals(listWithNewRow, modelList);
    }

    @Test
    public void deleteRowArrayIsNotEmpty() {
        int index = 1;
        List<TrajectoryRow> listWithoutRow = new ArrayList<>(list);
        listWithoutRow.remove(index);
        model.deleteRow(index);
        List<TrajectoryRow> modelList = model.getTrajectoryRowList();
        Assert.assertEquals(listWithoutRow, modelList);
    }

    @Test
    public void deleteRowArrayIsEmpty() {
        int index = 0;
        List<TrajectoryRow> listWithoutRow = new ArrayList<>(list);
        listWithoutRow.remove(index);
        listWithoutRow.remove(index);
        listWithoutRow.remove(index);
        model.deleteRow(index);
        model.deleteRow(index);
        model.deleteRow(index);
        List<TrajectoryRow> modelList = model.getTrajectoryRowList();
        Assert.assertEquals(listWithoutRow, modelList);
    }

    @Test
    public void sortByTimeNewFirstRow() {
        List<TrajectoryRow> listWithNewRow = new ArrayList<>(list);
        TrajectoryRow emptyRow = new TrajectoryRow(0, 0, 0, 0, 0, 0, 0);
        listWithNewRow.add(0, emptyRow);
        model.insertRowBelow(list.size() - 1);
        List<TrajectoryRow> modelList = model.getTrajectoryRowList();
        modelList.get(modelList.size() - 1).setTime(0);
        model.sortByTime();
        List<TrajectoryRow> sortedModelList = model.getTrajectoryRowList();
        Assert.assertEquals(listWithNewRow, sortedModelList);
    }
    @Test
    public void sortByTimeNewLastRow() {
        List<TrajectoryRow> listWithNewRow = new ArrayList<>(list);
        TrajectoryRow emptyRow = new TrajectoryRow(100, 0, 0, 0, 0, 0, 0);
        listWithNewRow.add(emptyRow);
        model.insertRowAbove(0);
        List<TrajectoryRow> modelList = model.getTrajectoryRowList();
        modelList.get(0).setTime(100);
        model.sortByTime();
        List<TrajectoryRow> sortedModelList = model.getTrajectoryRowList();
        Assert.assertEquals(listWithNewRow, sortedModelList);
    }
}
