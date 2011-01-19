package com.bottleworks.dailymoney.reports;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;

import com.bottleworks.dailymoney.R;
import com.bottleworks.dailymoney.data.AccountType;

public class BalanceYearTimeChart extends AbstractChart {

    public BalanceYearTimeChart(Context context, int orientation, float dpRatio) {
        super(context, orientation, dpRatio);
    }

    public Intent createIntent(String title, List<List<Balance>> yearTimebalance) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        int length = yearTimebalance.size();
        int seriesLength = 0;
        double max = 0;
        double min = 0;
        for (int i = 0; i < length; i++) {
            List<Balance> blist = yearTimebalance.get(i);
            Balance b1 = blist.get(0);
            XYSeries series = new XYSeries(b1.getName());
            seriesLength = blist.size();            
            for (int k = 0; k < seriesLength; k++) {
                Balance b = blist.get(k);
                series.add(b.getDate().getTime(), b.getMoney());
                max = Math.max(max,b.getMoney());
                min = Math.min(min,b.getMoney());
            }
            dataset.addSeries(series);
        }

        int[] colors = createColor(dataset.getSeriesCount());
        PointStyle[] styles = createPointStyle(dataset.getSeriesCount());
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
        
        renderer.setChartTitleTextSize(16*dpRatio);
        renderer.setAxisTitleTextSize(12*dpRatio);
        renderer.setLabelsTextSize(14*dpRatio);
        renderer.setLegendTextSize(14*dpRatio);
        renderer.setOrientation(Orientation.HORIZONTAL);//has bug in vertical
        renderer.setZoomEnabled(false, true);
        
        int top = (int)(renderer.getChartTitleTextSize()+3*dpRatio);
        int left = (int)(renderer.getAxisTitleTextSize()+ 55*dpRatio);
        int bottom = (int)(renderer.getAxisTitleTextSize()+45*dpRatio);
        int right = 1;
        
        renderer.setMargins(new int[]{top,left,bottom,right});
        int s = renderer.getSeriesRendererCount();
        for (int i = 0; i < s; i++) {
          ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
        }
        renderer.setYLabelsAlign(Align.RIGHT);
        renderer.setXLabelsAlign(Align.LEFT);
        
        renderer.setXLabels(16);
        renderer.setYLabels(16);
        renderer.setShowGrid(true);
        renderer.setXLabelsAngle(120);

        setChartSettings(renderer, title, "", i18n.string(R.string.label_money), yearTimebalance.get(0).get(0).getDate().getTime(),
                yearTimebalance.get(0).get(seriesLength-1).getDate().getTime(), min - (min/20), max+(max/20), Color.GRAY, Color.LTGRAY);
        renderer.setYLabels(10);
        return ChartFactory.getTimeChartIntent(context,dataset ,renderer, "yyyy MMM");
    }


}
