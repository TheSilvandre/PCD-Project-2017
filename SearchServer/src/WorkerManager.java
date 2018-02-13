import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class WorkerManager extends Thread{
    private Socket socket;
    private SearchServer searchServer;
    private SearchMotor searchMotor;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Integer currentArticle = -1;
    private int resultsBarrierPosition = 0;

    public WorkerManager(Socket socket, SearchServer searchServer, SearchMotor searchMotor) {
        this.socket = socket;
        this.searchServer = searchServer;
        this.searchMotor = searchMotor;
    }

    private synchronized void waitForSearch(){
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Awake");
    }

    @Override
    public void run() {
        Object inputObject;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            while(true) {   //Keeps thread running until there is an exception
                currentArticle = searchMotor.hasNextSearchAndDecrement();
                if (currentArticle == null) {
                    System.out.println("Waiting for searches");
                    waitForSearch();
                    continue;
                }
                System.out.println("Currently searching " + currentArticle);
                //Sends a new WorkerTask to the worker, with it's searched word and next article to search.
                outputStream.writeObject(new WorkerTask(searchMotor.getSearchedWord(), searchMotor.getNewsObject(currentArticle), searchMotor.getClientID()));

                inputObject = inputStream.readObject();
                if (inputObject instanceof WorkerSearchResult) {                 //Sent by Worker - Search Result - Should never be anything different then a WorkerSearchResult, but it's nice to check anyway!
                    //if(((WorkerSearchResult) inputObject).getOccurrences() != 0)
                    searchMotor.addSearchResult((WorkerSearchResult) inputObject);
                    System.out.println("Received result from Worker. Article: " + ((WorkerSearchResult) inputObject).getNewsArticleID() + " Client: " + ((WorkerSearchResult) inputObject).getClientID());
                } else {
                    System.out.println("Something strange was sent by the worker...");
                }

            }
        } catch (EOFException | SocketException eofException) {
            System.out.println("Socket closed by exception. Worker disconnected");
            //If socket closes by exception then the current article being searched needs to be placed into the pending list
            if(currentArticle != -1 && resultsBarrierPosition != -1)    //Add if there is an article being searched and there is a barrier position to place in to.
                System.out.println("Adding pending article - Article " + (currentArticle+1));
                searchMotor.addPendingArticle(resultsBarrierPosition, (currentArticle+1));
            try {
                socket.close();
                searchMotor.workerDisconnected(this);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Inside IO");
            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("IO");
        }
    }
}
