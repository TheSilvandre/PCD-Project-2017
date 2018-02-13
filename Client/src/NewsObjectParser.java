import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsObjectParser {
    public NewsObjectParser() {
    }

    public static int countOccurrences(String stringToFind, NewsObject newsObject) {
        int count = 0;

        for(Matcher bodyMatcher = Pattern.compile(stringToFind).matcher(newsObject.getBody()); bodyMatcher.find(); ++count) {
            ;
        }

        for(Matcher titleMatcher = Pattern.compile(stringToFind).matcher(newsObject.getTitle()); titleMatcher.find(); ++count) {
            ;
        }

        return count;
    }
}