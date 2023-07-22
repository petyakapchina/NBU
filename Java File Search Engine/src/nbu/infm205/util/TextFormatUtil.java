package nbu.infm205.util;

import nbu.infm205.dto.SearchResult;

import java.util.List;

public class TextFormatUtil {

    public static String formatSearchResults(List<SearchResult> foundResults, String searchText) {
        if (foundResults == null || foundResults.isEmpty()) {
            return "<b> No results found for search <i>'" + searchText + "'</i> </b>";
        }
        StringBuilder result = new StringBuilder("<b> Found Results for <i>'" + searchText + "'</i>: </b>");
        result.append("<table><tr><th>Thread</th><th>File Name</th><th>Times found</th></tr>");

        foundResults.stream().forEach(e -> {
            result.append("<tr><td>" + e.getThread() + "</td>");
            result.append("<td>" + e.getFile() + "</td>");
            result.append("<td>" + e.getFoundTimes() + "</td></tr>");
        });

        result.append("<table>");

        return result.toString();
    }
}
