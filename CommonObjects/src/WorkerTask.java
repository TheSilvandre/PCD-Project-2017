import java.io.Serializable;

/**
 * Simple worker task. Has the word that needs to be searched and the news object to search on.
 */
public class WorkerTask implements Serializable{
    private String searchedWord;
    private NewsObject newsObject;
    private long clientID;

    /**
     * Constructor
     * @param searchedWord Word to be searched for.
     * @param newsObject News article to search in.
     */
    public WorkerTask(String searchedWord, NewsObject newsObject, long clientID) {
        this.searchedWord = searchedWord;
        this.newsObject = newsObject;
        this.clientID = clientID;
    }

    public NewsObject getNewsObject() {
        return newsObject;
    }

    public String getSearchedWord() {
        return searchedWord;
    }

    public long getClientID() {
        return clientID;
    }
}
