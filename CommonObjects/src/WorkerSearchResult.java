import java.io.Serializable;

/**
 * Object to be returned from a Worker after he is done searching
 */
public class WorkerSearchResult implements Serializable{

    private String title;
    private int occurrences;
    private int newsArticleID;
    private long clientID;

    /**
     * Constructor
     * @param occurrences Number of occurrences of the word that was searched.
     * @param newsArticleID ID of the News article
     * @param clientID ID of the client that requested the search
     */
    public WorkerSearchResult(String title, int occurrences, int newsArticleID, long clientID) {
        this.title = title;
        this.occurrences = occurrences;
        this.newsArticleID = newsArticleID;
        this.clientID = clientID;
    }

    public WorkerSearchResult(NewsObject newsObject, int occurrences, long clientID){
        this.title = newsObject.getTitle();
        this.occurrences = occurrences;
        this.newsArticleID = newsObject.getID();
        this.clientID = clientID;
    }

    public int getOccurrences() {
        return occurrences;
    }

    public int getNewsArticleID() {
        return newsArticleID;
    }

    public long getClientID() {
        return clientID;
    }

    @Override
    public String toString() {
        return occurrences + " - " + title;
    }
}