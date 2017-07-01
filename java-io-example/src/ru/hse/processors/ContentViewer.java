package ru.hse.processors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Project: java-io-example
 * Author:  Surovtsev Maxim
 * Group:   BSE151(1)
 * Date:    22.06.17
 */
public class ContentViewer implements FileProcessor {
    private File file;

    public ContentViewer(File file) {
        this.file = file;
    }

    @Override
    public void process() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
