package nbu.infm205.dto;

public class SearchResult {

    private final String thread;
    private final String file;
    private int foundTimes;

    public SearchResult(final String threadId, final String file) {
        this.thread = threadId;
        this.file = file;
    }

    public String getThread() {
        return this.thread;
    }

    public String getFile() {
        return this.file;
    }

    public int getFoundTimes() {
        return foundTimes;
    }

    public void setFoundTimes(int foundTimes) {
        this.foundTimes = foundTimes;
    }
}
