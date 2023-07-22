package nbu.infm205.service;

import java.io.File;
import java.util.List;

public interface FileService {

    List<File> getFilesToSearchIn(String searchDir);

    int countFilesInDir();
}
