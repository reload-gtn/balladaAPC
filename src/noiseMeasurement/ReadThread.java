package noiseMeasurement;

import javax.swing.*;
import java.awt.*;

public class ReadThread implements Runnable{
    @Override
    public void run() {
        while(!NoiseMeasurement.stopCapture){
            try {
                Thread.sleep(NoiseMeasurement.time_update);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // обновление графика частотной зависимости
            Chart2D_freq.ac.update_panel();

            // установка тектовых значений измеряемых параметров
            // ControllerNoiseMeasurement.overallNoiseLevel.setText(String.valueOf(NoiseMeasurement.dF_dB.format(NoiseMeasurement.total_fast_ac)));
            System.out.println(NoiseMeasurement.dF_dB.format(NoiseMeasurement.total_fast_ac));

        }
    }
}