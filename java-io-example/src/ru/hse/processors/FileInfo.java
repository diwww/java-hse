package ru.hse.processors;

import java.io.File;
import java.time.LocalDate;
import java.util.Date;
import java.util.StringJoiner;

/**
 * Project: java-io-example
 * Author:  Surovtsev Maxim
 * Group:   BSE151(1)
 * Date:    22.06.17
 */
public class FileInfo implements FileProcessor {

    private File file;
    private String description;

    public FileInfo(File file) {
        this.file = file;
    }

    @Override
    public void process() {
        StringJoiner joiner = new StringJoiner("\n", "---\n", "\n---");
        joiner.add("Name:\t" + file.getName());
        joiner.add("Date:\t" + new Date(file.lastModified()));
        description = joiner.toString();
    }

    public String getDescription() {
        return description;
    }
}
