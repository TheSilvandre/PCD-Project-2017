import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NewsObjectParser {

    private NewsObjectParser(){ } //Empty private constructor so this Class can't be instanciated. Only to be used as a "static class"

    public static int countOccurrences(String stringToFind, NewsObject newsObject) {
        return countOccurrences(stringToFind, newsObject.getTitle(), newsObject.getBody());
    }

    public static int countOccurrences(WorkerTask task) {
        return countOccurrences(task.getSearchedWord(), task.getNewsObject().getTitle(), task.getNewsObject().getBody());
    }

    /**
     * @param searchedWord The word to be counted
     * @param title The title of the article
     * @param body The body of the article
     * @return The amount of times a sub-String exists in an article
     */
    private static int countOccurrences(String searchedWord, String title, String body) {
        int count = 0;

        final Matcher bodyMatcher = Pattern.compile(searchedWord).matcher(body);
        while (bodyMatcher.find()) {
            count++;
        }

        final Matcher titleMatcher = Pattern.compile(searchedWord).matcher(title);
        while(titleMatcher.find()){
            count++;
        }

        return count;
    }
}