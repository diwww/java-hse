package ru.hse.streams;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.LongStream;

/**
 * Project: StreamTestAPI
 * Author:  Surovtsev Maxim
 * Group:   BSE151(1)
 * Date:    25.05.17
 */
public class PrimeCalculatorLong implements PrimeCalculator {

    private int lastDigit;
    private String path;
    private boolean parallel;
    private LongStream stream;

    public PrimeCalculatorLong(String sBegin, String sEnd, String sLastDigit, String path, boolean parallel) {
        this.path = path;
        this.parallel = parallel;
        if(sLastDigit.length() != 1)
            throw new IllegalArgumentException();
        this.lastDigit = Integer.parseInt(sLastDigit);
        long begin = Long.parseLong(sBegin);
        long end = Long.parseLong(sEnd);
        stream = LongStream.range(begin, end + 1);
    }

    private boolean endsWith(long num) {
        return num % 10 == lastDigit;
    }

    private boolean isPrime(long num) {
        for (int i = 2; i <= num / 2; i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void calculate() {
        // Метод, возвращающий Stream - промежуточный,
        // Метод, возвращающий что-либо другое - терминальный.
        try (PrintWriter writer = new PrintWriter(path)) {
            long[] result;
            if (parallel) {
                result = stream.parallel().filter(i -> endsWith(i) && isPrime(i)).toArray();
            } else {
                result = stream.filter(i -> endsWith(i) && isPrime(i)).toArray();
            }
            writer.printf(Locale.getDefault(), "%d: %s\n", result.length, Arrays.toString(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
