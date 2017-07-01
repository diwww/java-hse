package ru.hse.find;

import java.io.File;
import java.util.List;

public interface Finder {
    List<File> find(String name);
}
