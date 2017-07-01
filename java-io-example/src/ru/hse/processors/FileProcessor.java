package ru.hse.processors;

import java.io.File;

public interface FileProcessor {
    void process();

    default String getDescription() {
        return null;
    }
}
