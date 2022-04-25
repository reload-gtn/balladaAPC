package noiseMeasurement;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;

public class Chart2D_freq {

    XYSeries createXYSeries() {
        XYSeries xySeries = new XYSeries("Шум");
        for (int i = 0; i < FrequencyAveraging.fc.length; i++) {
            xySeries.add(FrequencyAveraging.fc[i], NoiseMeasurement.spectre_1by3_fast_ac[i]);
        }
        return xySeries;
    }

     private XYSeriesCollection createDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(createXYSeries());
        return dataset;
    }

    private JFreeChart createChart(XYSeriesCollection dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Акустический шум",             // chart title
                "Category",                 // domain axis label
                "Value",                    // range axis label
                dataset,                             // data
                PlotOrientation.VERTICAL,
                false,                     // include legend
                true,
                false
        );
        chart.getTitle().setVisible(false);
        XYPlot plot = chart.getXYPlot();
        plot.getRenderer().setSeriesPaint(0, Color.BLUE);
        plot.getRenderer().setSeriesPaint(1, Color.RED);
        Marker marker_f_low = new ValueMarker(FrequencyAveraging.fc[NoiseMeasurement.index_of_f_low]);
        Marker marker_f_hight = new ValueMarker(FrequencyAveraging.fc[NoiseMeasurement.index_of_f_hight]);
        marker_f_low.setPaint(Color.GREEN);
        marker_f_hight.setPaint(Color.GREEN);
        marker_f_low.setStroke(new BasicStroke(1.5f));
        marker_f_hight.setStroke(new BasicStroke(1.5f));
        marker_f_low.setLabel("fн");
        marker_f_hight.setLabel("fв");
        marker_f_low.setLabelBackgroundColor(Color.white);
        marker_f_low.setLabelFont(new Font("Serif", Font.BOLD, 12));
        marker_f_hight.setLabelBackgroundColor(Color.white);
        marker_f_hight.setLabelFont(new Font("Serif", Font.BOLD, 12));
        plot.addDomainMarker(marker_f_low);
        plot.addDomainMarker(marker_f_hight);
        NumberAxis xAxis = new LogarithmicAxis("Частота, Гц");
        NumberAxis yAxis = new NumberAxis("Уровень, дБ");
        yAxis.setRange(NoiseMeasurement.minYrange,NoiseMeasurement.maxYrange);
        xAxis.setRange(NoiseMeasurement.minXrange,NoiseMeasurement.maxXrange);
        plot.setDomainAxis(xAxis);
        plot.setRangeAxis(yAxis);
        chart.setBackgroundPaint(Color.white);          // цвет области графика
        plot.setDomainGridlinePaint(Color.black);       // цвет сетки по оси х
        plot.setRangeGridlinePaint(Color.black);        // цвет сетки по оси y
        plot.setBackgroundPaint(Color.white);           // цвет фона области построения
        return chart;
    }

    public JPanel createDemoPanel() {
        JFreeChart chart = createChart(createDataset());
        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(NoiseMeasurement.dependence_graphsize);
        return panel;
    }

    JFrame frame = new JFrame("Частотная зависимость");

    void freq_dependence(){
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //NoiseMeasurement.freqdependence_graph.setSelected(false);
            }
        });
    }

    void update_panel(){
        this.frame.getContentPane().removeAll();
        this.frame.getContentPane().revalidate();
        this.frame.getContentPane().add(createDemoPanel());
        this.frame.getContentPane().revalidate();
    }

    Chart2D_freq() {

    }

    static Chart2D_freq ac = new Chart2D_freq();

}