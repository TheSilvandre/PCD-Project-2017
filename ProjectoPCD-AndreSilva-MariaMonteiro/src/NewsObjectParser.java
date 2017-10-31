import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsObjectParser {

    /*private String stringToFind;
    private int counter = 0;
    private NewsObject newsObject;

    public NewsObjectParser(String stringToFind, NewsObject newsObject) {
        this.stringToFind = stringToFind;
        this.newsObject = newsObject;
    }*/

    public static int countOccurences(String stringToFind, NewsObject newsObject) {
        int count = 0;

        final Matcher bodyMatcher = Pattern.compile(stringToFind).matcher(newsObject.getBody());
        while (bodyMatcher.find()) {
            count++;
        }

        final Matcher titleMatcher = Pattern.compile(stringToFind).matcher(newsObject.getTitle());
        while(titleMatcher.find()){
            count++;
        }

        return count;
    }
}