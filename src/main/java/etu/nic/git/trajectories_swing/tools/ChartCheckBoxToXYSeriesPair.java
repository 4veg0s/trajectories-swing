package etu.nic.git.trajectories_swing.tools;

import org.jfree.data.xy.XYSeries;

import javax.swing.*;

public class ChartCheckBoxToXYSeriesPair {
    private JCheckBox checkBox;
    private XYSeries series;

    public ChartCheckBoxToXYSeriesPair(JCheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public ChartCheckBoxToXYSeriesPair(JCheckBox checkBox, XYSeries series) {
        this.checkBox = checkBox;
        this.series = series;
    }

    public void setCheckBox(JCheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public void setSeries(XYSeries series) {
        this.series = series;
    }

    public JCheckBox getCheckBox() {
        return checkBox;
    }

    public XYSeries getSeries() {
        return series;
    }

    public XYSeries getSeriesIfCheckBoxSelectedOrNullOtherwise() {
        if (this.checkBox.isSelected()) {
            return this.series;
        } else {
            return null;
        }
    }
}
