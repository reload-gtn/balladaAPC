package noiseMeasurement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerNoiseMeasurement implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        noiseFrequencyDependenceInit();
        btnMeasurement.setDisable(false);
        overallNoiseLevel.setText("0");

    }

    private void noiseFrequencyDependenceInit() {
        NoiseMeasurement.get_device_number();
        noiseFrequencyDependence.getXAxis().setLabel("Частота, Гц");
        noiseFrequencyDependence.getYAxis().setLabel("Уровень шума, дБ");
        cbAverageTime.getItems().add("fast");
        cbAverageTime.getItems().add("slow");
        noiseFrequencyDependence.setLegendVisible(false);
        noiseFrequencyDependence.setCreateSymbols(false);                   // маркеры по точкам
        double[] var_PS40 = new double[]{ 70, 65, 61, 57.5, 54, 51, 48, 45.8, 44, 42.4, 40.4, 39, 37.3, 36, 35, 34, 33, 32, 31, 30.5, 30, 29.5, 29, 28.5, 28, 27.8, 27.5};   // частотная зависимость ПС в третьоктавах
        String[] f_1by3_gost = new String[]{"40", "50", "63", "80", "100", "125", "160", "200", "250", "315", "400", "500", "630", "800", "1к", "1,25к", "1,6к", "2к", "2,5к", "3,15к", "4к", "5к", "6,3к", "8к", "10к", "12,5к", "16к"};  // третьоктавы по ГОСТУ
        XYChart.Series series = new XYChart.Series();
        for(int i = 0; i<var_PS40.length; i++){
            series.getData().add(new XYChart.Data(f_1by3_gost[i], var_PS40[i]));
        }
        noiseFrequencyDependence.getData().addAll(series);
    }

    public Button btnMeasurement;
    public Label overallNoiseLevel;
    public LineChart noiseFrequencyDependence;
    public ChoiceBox<String> cbAverageTime;

    public void btnMeasurementEnable(){
        NoiseMeasurement.init_mixer();
        // запуск потока чтения данныз с ЗК
        NoiseMeasurement.init_capture();
        // запуск потока чтения расчетных данных параметров
        NoiseMeasurement.init_readcapture();
        // запуск построения временной зав
    }

}
