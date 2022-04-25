package noiseMeasurement;

class CaptureThread implements  Runnable{

    //длина буфера чтения
    public  void run() {
        while(!NoiseMeasurement.stopCapture){
            double[] array_to_1by3_ac;
            double[] array_to_1by3_vib;
            int cnt = NoiseMeasurement.microphoneLine.read(NoiseMeasurement.buffer, 0, NoiseMeasurement.buffer.length);
            if (cnt > 0) {
                Complex[] left, right;
                Complex[] left_buf = new Complex[NoiseMeasurement.fft_size];                 // левый канал (акустика)
                Complex[] right_buf = new Complex[NoiseMeasurement.fft_size];                // правый канал (вибрация)
                int count = 0;
                for (int i = 0; i < cnt; i = i + 4) {
                    int leftcanal = NoiseMeasurement.buffer[i] & 0x00FF | NoiseMeasurement.buffer[i + 1] << 8;
                    int rightcanal = NoiseMeasurement.buffer[i + 2] & 0x00FF | NoiseMeasurement.buffer[i + 3] << 8;
                    left_buf[count] = new Complex(leftcanal, 0);
                    right_buf[count] = new Complex(rightcanal, 0);
                    count++;
                }
                left = left_buf;
                right = right_buf;

                Complex[] data_ac = FFT.fft(left);
                Complex[] data_vib = FFT.fft(right);
                double[] spectre_ac = new double[NoiseMeasurement.fft_size];
                double[] spectre_vib = new double[NoiseMeasurement.fft_size];
                for (int i = 0; i < data_vib.length; i++) {
                    spectre_ac[i] = 2 * data_ac[i].abs() / NoiseMeasurement.fft_size;
                    spectre_vib[i] = 2 * data_vib[i].abs() / NoiseMeasurement.fft_size;
                }

                if (NoiseMeasurement.var_time == NoiseMeasurement.slow_time) {
                    array_to_1by3_ac = FrequencyAveraging.array_to_1by3(Calculations.ac.spectre_avr(spectre_ac, NoiseMeasurement.n_slow_fft));
                    array_to_1by3_vib = FrequencyAveraging.array_to_1by3(Calculations.vib.spectre_avr(spectre_vib, NoiseMeasurement.n_slow_fft));}
                else {
                    array_to_1by3_ac = FrequencyAveraging.array_to_1by3(Calculations.ac.spectre_avr(spectre_ac, NoiseMeasurement.n_fast_fft));
                    array_to_1by3_vib = FrequencyAveraging.array_to_1by3(Calculations.vib.spectre_avr(spectre_vib, NoiseMeasurement.n_fast_fft));
                }

                NoiseMeasurement.total_fast_ac = Calculations.one_to_dB(FrequencyAveraging.array_1by3_crop_to_total(array_to_1by3_ac));
                NoiseMeasurement.total_fast_vib = Calculations.one_to_dB(FrequencyAveraging.array_1by3_crop_to_total(array_to_1by3_vib));

                NoiseMeasurement.spectre_1by3_fast_ac = Calculations.mass_to_dB(array_to_1by3_ac);
                NoiseMeasurement.spectre_1by3_fast_vib = Calculations.mass_to_dB(array_to_1by3_vib);
            }
        }
        NoiseMeasurement.microphoneLine.stop();
        NoiseMeasurement.microphoneLine.close();
    }
}
