package ru.hse.streams;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Stream;

/**
 * Project: StreamTestAPI
 * Author:  Surovtsev Maxim
 * Group:   BSE151(1)
 * Date:    27.05.17
 */
public class PrimeCalculatorBigInteger implements PrimeCalculator {

    private BigInteger lastDigit;
    private String path;
    private boolean parallel;
    private Stream<BigInteger> stream;

    public PrimeCalculatorBigInteger(String sBegin, String sEnd, String sLastDigit, String path, boolean parallel) {
        this.path = path;
        this.parallel = parallel;
        if (sLastDigit.length() != 1)
            throw new IllegalArgumentException();
        this.lastDigit = new BigInteger(sLastDigit);
        BigInteger begin = new BigInteger(sBegin);
        BigInteger end = new BigInteger(sEnd);
        ArrayList<BigInteger> list = new ArrayList<>();
        for (BigInteger i = begin; i.compareTo(end) < 0; i = i.add(BigInteger.ONE)) {
            list.add(i);
        }
        stream = list.stream();
    }

    private boolean endsWith(BigInteger num) {
        return num.mod(BigInteger.TEN).equals(lastDigit);
    }

    @Override
    public void calculate() {
        try (PrintWriter writer = new PrintWriter(path)) {
            Object[] result;
            if (parallel) {
                result = stream.parallel().filter(i -> endsWith(i) && i.isProbablePrime(5)).toArray();
            } else {
                result = stream.filter(i -> endsWith(i) && i.isProbablePrime(5)).toArray();
            }
            writer.printf(Locale.getDefault(), "%d: %s\n", result.length, Arrays.toString(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
