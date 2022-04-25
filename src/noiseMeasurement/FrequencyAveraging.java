package noiseMeasurement;

public class FrequencyAveraging {

    static int number_of_fc = 27;                   // число третьоктавных полос
    static int k_of_fs = -(number_of_fc+1)/2;       // коэффициент расчета ценральной частоты
    static double k_1by3_octave = 0.231;            // коэффициент осотношения третьоктавных полос
    static double k_1by1_octave = 0.707;            // коэффициент осотношения октавных полос
    static double[] fc = new double[number_of_fc];  // центральные частоты полос
    static double[] fl = new double[number_of_fc];  // нижние частоты полос
    static double[] fh = new double[number_of_fc];  // верхние частоты полос

    //инициализация центральных и боковых частот третьоктавного ряда (вызов из основного главного класса программы)
    public static void init_f(){
        for (int i = 0; i < number_of_fc; i++) {fc[i]=1000*Math.pow(10,((double)(k_of_fs+i)/10));}          // расчет центральных частот
        for (int i = 0; i < fc.length; i++) {fl[i]=k_1by3_octave*fc[i]/((Math.pow(2,(double) 1/3)-1));}     // расчет нижних частот
        for (int i = 0; i < fc.length; i++) {fh[i]=fl[i]*Math.pow(2,(double) 1/3);}                         // расчет верхних частот
    }

    // третьоктавная фильтрация входного массива результата расчета fft
    synchronized static double[] array_to_1by3(double[] in_data){
        double[] array_1by3 = new double[number_of_fc];
        double k_f = (double) NoiseMeasurement.sample_Rate/NoiseMeasurement.fft_size;     // коэффициент дискретности по частоте
        for(int i = 0; i < number_of_fc; i++) {
            for(int j = 0; j < in_data.length; j++) {
                if((j*k_f > fl[i]) && (j*k_f < fh[i]))
                    array_1by3[i] = array_1by3[i]+in_data[j]*in_data[j];
            }
            array_1by3[i] = Math.sqrt(array_1by3[i]);
        }
        return array_1by3;
    }

    // расчет общего уровня шума по всем третьоктавным полосам частот
    synchronized static double array_1by3_to_total(double[] in_data){
        double total = 0;
        for(int i = 0; i < in_data.length; i++) {
            total = total+in_data[i];
        }
        return total;
    }

    //расчет общего уровня шума в выбранных полосах третьоктавным частот с дб-ными значениями
    synchronized static double array_1by3_crop_db_to_total(double[] in_data){
        double total = 0;
        for(int i = NoiseMeasurement.index_of_f_low; i <= NoiseMeasurement.index_of_f_hight; i++) {
            total = total+Math.pow(Math.pow(10,in_data[i]/20),2);
        }
        return Math.sqrt(total);
    }


    // расчет общего уровня шума в выбранных полосах третьоктавным частот
    synchronized static double array_1by3_crop_to_total(double[] in_data){
        double total = 0;
        for(int i = NoiseMeasurement.index_of_f_low; i <= NoiseMeasurement.index_of_f_hight; i++) {
            total = total+Math.pow(in_data[i],2);
        }
        return Math.sqrt(total);
    }

}
