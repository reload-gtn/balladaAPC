package noiseMeasurement;

import java.awt.Dimension;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

public class NoiseMeasurement extends Application {

    public static boolean stopCapture = true;       // условие остановки захвата данных со звуковой карты
    public static TargetDataLine microphoneLine;    // канал данных захвата аудио

    // параметры аудиоформата записи сигналов звуковой карты
    public static int sample_Rate = 44100;              // частота дискретизации
    public static int size_of_sample_in_bit = 16;       // битность сигнала
    public static int number_channels = 2;              // число каналов

    static int index_of_f_low   =   14;         // индекс нижней полосы частот анализируемого диапазона
    static int index_of_f_hight =   23;         // индекс верхней полосы частот анализируемого диапазона

    // количество линий FFT анализа
    static int fft_size = 4096;
    static int buffer_len = fft_size*4;
    static byte[] buffer = new byte[buffer_len];

    static double fast_time = 0.5;      // быстрое время усрднения
    static double slow_time = 2.0;      // медленное время усреднения
    static double var_time = fast_time; // выставленное время усреднения
    static int n_fast_fft = (int) (fast_time * sample_Rate/fft_size);   // определение количества fft спектров за быстрое время
    static int n_slow_fft = (int) (slow_time * sample_Rate/fft_size);   // определение количества fft спектров за медленное время

    // анализируемые параметры
    static double[] spectre_1by3_fast_ac = new double[FrequencyAveraging.fc.length];    // третьоктавный усредненный спектр акустического шума
    static double[] spectre_1by3_fast_vib = new double[FrequencyAveraging.fc.length];   // третьоктавный усредненный спектр вибрационного шума
    static double total_fast_ac;                                                        // общий уровень акустического шума
    static double total_fast_vib;                                                       // общий уровень вибрационного шума
    static int time_update = 100;                                                       // время обновления в мс данных в окнах (текст и графика)
    static double mean;                                                                 // среднее значение уровня
    static double variation;                                                            // коэффициент вариации уровня

    static Dimension dependence_graphsize   = new Dimension(500,260);
    static int minYrange    = 0;
    static int maxYrange    = 100;
    static int minXrange    = 40;
    static int maxXrange    = 16000;

    // определение формата данных аудио
    public static final AudioFormat FORMAT = new AudioFormat(sample_Rate, size_of_sample_in_bit, number_channels, true, false);

    //формат вывода значений уровней шума
    static DecimalFormat dF_dB = new DecimalFormat( "#" );
    static DecimalFormat dF_var = new DecimalFormat( "#.#" );

    //получить номер микшера для записи их списка
    static int get_device_number(){
        int n = 0;
        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
        for (int i = 0; i < mixerInfo.length; i++) {
            try {
                System.out.println((new String(mixerInfo[i].getName().getBytes("Windows-1252"), "Windows-1251")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return n;
    }

    public static void init_mixer(){
        try {
            DataLine.Info lineInfo = new DataLine.Info(TargetDataLine.class, FORMAT);
            Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
            Mixer mixerS = AudioSystem.getMixer(mixerInfos[1]); // Здесь пишем номер устройства на которое будем посылать звук
            microphoneLine = (TargetDataLine) mixerS.getLine(lineInfo);
            microphoneLine.open(FORMAT);
            microphoneLine.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    // открытие потока чтения данных со звуковой карты и расчета
    static void init_capture(){
        Thread captureThread;
        captureThread = new Thread(new CaptureThread());
        captureThread.start();
        //captureThread.interrupt();
    }

    // открытие потока чтения и расчета данных программой
    static void init_readcapture(){
        // запуск потока чтения расчетных данных
        Thread testThread;
        testThread = new Thread(new ReadThread());
        // значение задержки времени расчета данных на время усреднения результатов
        int delay = (int)(var_time*1000); // milliseconds
        ActionListener taskPerformer =
                evt -> testThread.start();
        Timer timer = new Timer(delay, taskPerformer);
        timer.setRepeats(false);
        timer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("NoiseMeasurement.fxml"));
        primaryStage.setTitle("Измерение шума");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
