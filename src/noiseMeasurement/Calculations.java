package noiseMeasurement;

public class Calculations {

    // спектры fft
    double[] spectre_avr_fast = new double[NoiseMeasurement.fft_size];                           // единичный средний спектр за быстрое время
    double[] spectre_avr_slow = new double[NoiseMeasurement.fft_size];                           // единичный средний спектр за быстрое время
    double[][] spectre_avr_fast_mass = new double[NoiseMeasurement.fft_size][NoiseMeasurement.n_fast_fft];    // массив спектров за быстрое время
    double[][] spectre_avr_slow_mass = new double[NoiseMeasurement.fft_size][NoiseMeasurement.n_slow_fft];    // массив спектров за медленное время

    //перевод спектральных значений массива в уровни дБ
    synchronized public static double[] mass_to_dB(double[] data_in){
        double[] data_out = new double[data_in.length];
        for(int i = 0; i < data_in.length; i++) {
             {
                data_out[i] = one_to_dB(data_in[i]);
            }
        }
        return data_out;
    }

    //перевод одного интового значения в уровень дБ
    synchronized public static double one_to_dB(double data_in){
        return 20*Math.log10(data_in/32768);
    }

    synchronized public static double one_PS_to_dB(double data_in){
        return 20*Math.log10(data_in);
    }

    //усреднение спектра (расчет из двумерного массива размером fft_size на количество блоков fft
    synchronized public double[] spectre_avr(double[] in_array, int n_fft) {
        if (n_fft == NoiseMeasurement.n_slow_fft) {
            for (int i = 0; i < in_array.length; i++) {
                if (n_fft - 1 >= 0) System.arraycopy(spectre_avr_slow_mass[i], 0, spectre_avr_slow_mass[i], 1, n_fft - 1);
            }
            for (int i = 0; i < in_array.length; i++) {
                spectre_avr_slow_mass[i][0] = in_array[i];
            }
            for (int i = 0; i < in_array.length; i++) {
                spectre_avr_slow[i] = average(spectre_avr_slow_mass[i]);
            }
            return spectre_avr_slow; }
        else {
            for (int i = 0; i < in_array.length; i++) {
                if (n_fft - 1 >= 0)
                    System.arraycopy(spectre_avr_fast_mass[i], 0, spectre_avr_fast_mass[i], 1, n_fft - 1);
            }
            for (int i = 0; i < in_array.length; i++) {
                spectre_avr_fast_mass[i][0] = in_array[i];
            }
            for (int i = 0; i < in_array.length; i++) {
                spectre_avr_fast[i] = average(spectre_avr_fast_mass[i]);
            }
            return spectre_avr_fast;
        }
    }

    // расчет среднего значения массива элементов
    synchronized static double average(double[] in_array) {
        double sum = 0;
        if (in_array.length > 0) {
            for (double v : in_array) {
                sum += v / in_array.length;
            }
        }
        return sum;
    }

    static String[] mass_double_to_string(double[] mass_double){
        String[] mass_string = new String[mass_double.length];
        for (int i = 0; i < mass_double.length; i++){
            mass_string[i] = String.valueOf(NoiseMeasurement.dF_dB.format(mass_double[i]));
        }
        return mass_string;
    }

    //очистка массива
    static double[][] mass_empty(int var_time) {
        return new double[NoiseMeasurement.fft_size][var_time];
    }

    static Calculations ac = new Calculations();           // объект со спектрами для акустики
    static Calculations vib = new Calculations();          // объект со спектрами для вибрации

}
