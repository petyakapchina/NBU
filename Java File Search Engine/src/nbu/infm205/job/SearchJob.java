package nbu.infm205.job;

import nbu.infm205.dto.SearchResult;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.Callable;

public class SearchJob implements Callable<SearchResult> {

    private final String searchString;
    private final String file;

    public SearchJob(final String searchString, final String file) {
        this.searchString = searchString.toLowerCase();
        this.file = file;
    }

    @Override
    public SearchResult call() {

        String threadName = Thread.currentThread().getName();
        threadName = threadName.substring(threadName.indexOf("t"));
        SearchResult result = new SearchResult(threadName, this.file);

        int found = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(this.file))) {
            String line = reader.readLine();

            while (line != null) {
                if (line.toLowerCase().contains(searchString)) {
                    found++;
                }
                line = reader.readLine();
            }
            result.setFoundTimes(found);
        } catch (IOException e) {

        }

        return result;
    }
}