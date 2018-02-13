import java.util.ArrayList;
import java.util.List;

/**
 * Handles search requests and distributes them between workers.
 */
public class SearchMotor{

    private List<NewsObject> newsObjects;

    private ArrayList<WorkerManager> workerList = new ArrayList<>();

    private ArrayList<ResultsBarrier> resultsList = new ArrayList<>();

    /**
     * Constructor
     * @param newsObjects List of all news articles.
     */
    public SearchMotor(List<NewsObject> newsObjects) {
        this.newsObjects = newsObjects;
    }

    public NewsObject getNewsObject(int inputObject) {
        return newsObjects.get(inputObject);
    }

    public void registerWorker(WorkerManager workerManager){
        workerList.add(workerManager);
    }

    public synchronized void addSearchResult(WorkerSearchResult searchResult) {
        for(ResultsBarrier barrier: resultsList){
            if(barrier.getClientID() == searchResult.getClientID()){
                barrier.addSearchResult(searchResult);
                System.out.println(barrier.getReadyResults().get() + " / " + barrier.getNumberOfArticles());
                if(barrier.getReadyResults().get() == barrier.getNumberOfArticles()) {
                    notifyAll();
                }
                break;
            }
        }
    }

    public void workerDisconnected(WorkerManager workerManager) {
        workerList.remove(workerManager);
    }

    public void addPendingArticle(int resultsPosition, int article) {
        resultsList.get(resultsPosition).addPendingArticle(article);
    }

    public synchronized Integer hasNextSearchAndDecrement() throws InterruptedException {
        while(noSearchableArticles()) {
            System.out.println("No searchable articles");
            wait();
        }
        return resultsList.get(0).getNextSearch();

    }

    private boolean noSearchableArticles(){
        if(resultsList.isEmpty()){
            return true;
        } else {
            return (resultsList.get(0).getResults().size() == resultsList.get(0).getNumberOfArticles()) && resultsList.get(0).getPendingArticles().isEmpty();
        }
    }

    public String getSearchedWord() throws InterruptedException{
        return resultsList.get(getNextResultsBarrierPosition()).getSearchedWord();
    }

    public long getClientID() throws InterruptedException {
        return resultsList.get(getNextResultsBarrierPosition()).getClientID();
    }

    public synchronized void addSearch(ClientManager manager, String searchedWord, long clientID){
        resultsList.add(new ResultsBarrier(newsObjects.size(), manager, searchedWord, clientID, this));
        this.notifyAll();
    }

    public synchronized ArrayList<WorkerSearchResult> getResultsWhenReady(long clientID) throws InterruptedException {
        ArrayList<WorkerSearchResult> returnList = null;
        ResultsBarrier barrierToRemove = null;
        for(ResultsBarrier b : resultsList){
            if(b.getClientID() == clientID){
                while(b.getReadyResults().get() < b.getNumberOfArticles()){
                    wait();
                }
                returnList = (ArrayList<WorkerSearchResult>) b.getResults().clone();
                barrierToRemove = b;
                break;
            }
        }
        if(barrierToRemove != null)
            resultsList.remove(barrierToRemove);
        return returnList;
    }

    public synchronized int getNextResultsBarrierPosition() throws InterruptedException {
        while(resultsList.isEmpty()){
            wait();
        }
        return 0;
    }
}
