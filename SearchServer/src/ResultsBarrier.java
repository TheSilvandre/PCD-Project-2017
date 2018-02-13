import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ResultsBarrier{

    private ArrayList<WorkerSearchResult> results = new ArrayList<>();
    private ArrayList<Integer> pendingArticleList = new ArrayList<>();
    private final int numberOfArticles;
    private AtomicInteger searchesRemaining;
    private AtomicInteger readyResults = new AtomicInteger(0);
    private ClientManager manager;
    private SearchMotor searchMotor;
    private String searchedWord;
    private long clientID;

    public ResultsBarrier(int searchesRemaining, ClientManager manager, String searchedWord, long clientID, SearchMotor searchMotor) {
        this.searchesRemaining = new AtomicInteger(searchesRemaining);
        this.numberOfArticles = searchesRemaining;
        this.manager = manager;
        this.searchedWord = searchedWord;
        this.clientID = clientID;
        this.searchMotor = searchMotor;
    }

    public long getClientID() {
        return clientID;
    }

    public synchronized void addSearchResult(WorkerSearchResult searchResult) {
        results.add(searchResult);
        readyResults.incrementAndGet();
    }

    public synchronized Integer getNextSearch() {
        //System.out.println("Pending articles : " + pendingArticleList.size());
        Integer i = searchesRemaining.decrementAndGet();
        if (i < 0) {
            System.out.println("No articles left, getting pending articles!");
            return getNextPendingArticle();
        } else {
            return i;
        }
    }

    public synchronized void addPendingArticle(int article) {
        pendingArticleList.add(article);
        System.out.println("Adding pending article, notifying!");
        synchronized (searchMotor) {
            searchMotor.notifyAll();
        }
    }

    public String getSearchedWord() {
        return searchedWord;
    }

    public ArrayList<WorkerSearchResult> getResults() {
        return results;
    }

    public int getNumberOfArticles() {
        return numberOfArticles;
    }

    public AtomicInteger getReadyResults() {
        return readyResults;
    }

    public ArrayList<Integer> getPendingArticles() {
        return pendingArticleList;
    }

    public synchronized Integer getNextPendingArticle() {
        if(pendingArticleList.isEmpty()){
            System.out.println("No pending articles, returning null!");
            return null;
        } else {
            System.out.println("Pending article found - Article " + pendingArticleList.get(0));
            return pendingArticleList.remove(0);
        }
    }
}