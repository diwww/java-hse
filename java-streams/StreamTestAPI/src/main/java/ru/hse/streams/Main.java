package ru.hse.streams;

import java.util.Locale;

/**
 * Project: StreamTestAPI
 * Author:  Surovtsev Maxim
 * Group:   BSE151(1)
 * Date:    25.05.17
 */
public class Main {

    private static String prompt = String.format(Locale.getDefault(),
            "Usage: %s %s %s %s %s %s\nPossible options:\n\t-p\tcompute in parallel\n\t-b\tuse BigInteger",
            "[PROGRAM_NAME]", "[OPTION]",
            "[BEGIN]", "[END]", "[LAST_DIGIT]",
            "[OUTPUT_FILE]");

    public static void main(String[] args) {
        try {
            if (args.length == 6 && args[0].equals("-p") && args[1].equals("-b")) {
                System.out.printf("Elapsed time = %d ms\n",
                        run(args[2], args[3], args[4], args[5], true, true));
            } else if (args.length == 6 && args[0].equals("-b") && args[1].equals("-p")) {
                System.out.printf("Elapsed time = %d ms\n",
                        run(args[2], args[3], args[4], args[5], true, true));
            } else if (args.length == 5 && args[0].equals("-b")) {
                System.out.printf("Elapsed time = %d ms\n",
                        run(args[1], args[2], args[3], args[4], false, true));
            } else if (args.length == 5 && args[0].equals("-p")) {
                System.out.printf("Elapsed time = %d ms\n",
                        run(args[1], args[2], args[3], args[4], true, false));
            } else if (args.length == 4) {
                System.out.printf("Elapsed time = %d ms\n",
                        run(args[0], args[1], args[2], args[3], false, false));
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println(prompt);
        }
    }

    public static long run(String sBegin, String sEnd, String sLastDigit, String outputFile, boolean par, boolean bigInt) {
        long startTime = System.currentTimeMillis();
        PrimeCalculator calculator;
        if (bigInt) {
            calculator = new PrimeCalculatorBigInteger(sBegin, sEnd, sLastDigit, outputFile, par);
        } else {
            calculator = new PrimeCalculatorLong(sBegin, sEnd, sLastDigit, outputFile, par);
        }
        calculator.calculate();
        long stopTime = System.currentTimeMillis();
        return stopTime - startTime;
    }
}
