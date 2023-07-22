package nbu.infm205.service;

public interface SearchEngineService {

    String search(final String searchText, final String searchDir);

    void setThreadsNumber(int runWithThreads);
}
