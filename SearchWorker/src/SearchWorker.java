import java.io.*;
import java.net.*;

public class SearchWorker {
    private final int SERVER_PORT;
    private final String SERVER_NAME;
    private final ConfigReader configReader = new ConfigReader("config.txt");

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public SearchWorker() {

        configReader.loadValues();

        SERVER_PORT = configReader.getServerPort();
        SERVER_NAME = configReader.getServerName();

        try {
            connect();
        } catch (UnknownHostException e) {
            System.out.println("Unknown host on " + SERVER_NAME + ":" + SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true){
            Object inputObject = null;
            WorkerSearchResult searchResult = null;
            try {
                inputObject = in.readObject();
            }  catch (SocketException socketException) {
                System.out.println("Server disconnected, attempting to reconnect");
                try {
                    socket.close();
                    socket = null;
                    connect();
                    Thread.sleep(1000);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("ClassNotFoundException - Maybe the class being sent over isn't the same as the one that exists on this project?");
            }

            if(inputObject instanceof WorkerTask){  //Should always be an instance of WorkerTask, but it's always good to check!
                System.out.println("User " + ((WorkerTask) inputObject).getClientID() + " searched: " + ((WorkerTask) inputObject).getSearchedWord() + " in article: " + ((WorkerTask) inputObject).getNewsObject().getID());
                searchResult = doSearch((WorkerTask) inputObject);
            }

            try {
                out.writeObject(searchResult);
            } catch (SocketException socketException) {
                System.out.println("Server disconnected, attempting to reconnect");
                try {
                    socket.close();
                    socket = null;
                    connect();
                    Thread.sleep(1000);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void connect() throws IOException{
        InetAddress address = InetAddress.getByName(SERVER_NAME);
        while(socket == null) {
            try {
                System.out.println("Attempting to connect to server on: " + SERVER_NAME + ":" + SERVER_PORT);
                socket = new Socket(address, SERVER_PORT);
            } catch (ConnectException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    System.out.println("Interrupted while sleeping!");
                }
            }
        }
        System.out.println("Connected!");
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    private WorkerSearchResult doSearch(WorkerTask task) {
        return new WorkerSearchResult(task.getNewsObject(), NewsObjectParser.countOccurrences(task), task.getClientID());
    }
}