package nbu.infm205.service.impl;

import nbu.infm205.service.FileService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DefaultFileService implements FileService {

    private static final String SEARCH_DIR = "resources";

    @Override
    public List<File> getFilesToSearchIn(String searchDir) {
        return getFilesFromDirectory(searchDir == null ? SEARCH_DIR : searchDir);
    }

    private List<File> getFilesFromDirectory(final String searchDir) {
        File directory = new File(searchDir);
        File[] fileList = directory.listFiles();

        final List<File> files = new ArrayList<>();

        for (File file : fileList) {
            if (file.isDirectory()) {
                files.addAll(this.getFilesFromDirectory(file.getAbsolutePath()));
            } else if (isTxtFileExtension(file.getName())) {
                files.add(file);
            }
        }

        return files;
    }

    public int countFilesInDir() {
        return countFilesInDir(SEARCH_DIR);
    }

    private int countFilesInDir(final String searchDir) {
        int filesCount = 0;
        File directory = new File(searchDir);
        File[] fileList = directory.listFiles();
        if (fileList != null) {
            for (File file : fileList) {
                if (file.isDirectory()) {
                    filesCount += this.countFilesInDir(file.getAbsolutePath());
                } else if (isTxtFileExtension(file.getName())) {
                    filesCount++;
                }
            }
        }

        return filesCount;
    }

    private boolean isTxtFileExtension(final String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return extension.equals("txt");
    }
}
