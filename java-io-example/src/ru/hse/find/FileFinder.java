package ru.hse.find;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileFinder implements Finder {

    private List<File> foundFiles;
    private File dir;

    /**
     * @param path - directory path
     */
    public FileFinder(String path) throws IOException {
        dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new IOException("Directory not found.");
        }
        foundFiles = new ArrayList<>();
    }

    @Override
    public List<File> find(String name) {
        foundFiles.clear();
        traverse(dir, name);
        return foundFiles;
    }


    private void traverse(File file, String name) {
        File[] files = file.listFiles();

        if (files == null) {
            if (checkFile(file, name))
                foundFiles.add(file);
            return;
        }

        for (File f : files) {
            traverse(f, name);
        }
    }

    private boolean checkFile(File file, String name) {
        return file.getName().contains(name);
    }

}
