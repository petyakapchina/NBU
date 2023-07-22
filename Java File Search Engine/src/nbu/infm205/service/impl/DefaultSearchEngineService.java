package nbu.infm205.service.impl;

import nbu.infm205.dto.SearchResult;
import nbu.infm205.job.SearchJob;
import nbu.infm205.service.FileService;
import nbu.infm205.service.SearchEngineService;
import nbu.infm205.util.TextFormatUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class DefaultSearchEngineService implements SearchEngineService {

    private final FileService fileService;

    private int threadsNumber;

    private List<File> files;
    private List<Future<SearchResult>> foundResults;

    public DefaultSearchEngineService(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public String search(final String searchText, final String searchDir) {
        try {
            this.foundResults = new ArrayList<>();
            this.files = this.fileService.getFilesToSearchIn(searchDir);
            createThreadPool(this.threadsNumber, searchText);
            while (!allFilesChecked()) {
                Thread.sleep(5);
            }
        } catch (Exception e) {
            System.out.println("Something went wrong. Returning empty result.");
        }
        List<SearchResult> searchResults = new ArrayList<>();

        for (Future future : foundResults) {
            try {
                SearchResult result = (SearchResult) future.get();
                if (result.getFoundTimes() > 0) {
                    searchResults.add(result);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return TextFormatUtil.formatSearchResults(searchResults, searchText);
    }

    private void createThreadPool(int threadsNumber, final String searchText) {
        ExecutorService executor = Executors.newFixedThreadPool(threadsNumber);
        for (File file : files) {
            Callable<SearchResult> thread = new SearchJob(searchText, file.getAbsolutePath());

            Future<SearchResult> result = executor.submit(thread);
            this.foundResults.add(result);
        }
        executor.shutdown();
    }

    private boolean allFilesChecked() {
        int isDone = this.foundResults.stream().filter(f -> f.isDone()).collect(Collectors.toList()).size();
        return isDone == this.foundResults.size();
    }

    @Override
    public void setThreadsNumber(int runWithThreads) {
        this.threadsNumber = runWithThreads;
    }
}
