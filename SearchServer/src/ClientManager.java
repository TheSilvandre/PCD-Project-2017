import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ClientManager extends Thread{
    private Socket socket;
    private SearchServer searchServer;
    private long clientID;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public ClientManager(Socket socket, SearchServer searchServer, long clientID) {
        this.socket = socket;
        this.searchServer = searchServer;
        this.clientID = clientID;
    }

    @Override
    public void run() {
        Object inputObject;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            while (true) {
                inputObject = inputStream.readObject();
                if (inputObject instanceof String) {    //Search Request
                    String inputString = (String) inputObject;
                    System.out.println("User sent: " + inputString);
                    searchServer.getSearchMotor().addSearch(this, inputString, clientID);
                    ArrayList<WorkerSearchResult> resultsList = searchServer.getSearchMotor().getResultsWhenReady(clientID);
                    resultsList.sort((o1, o2) -> o2.getOccurrences() - o1.getOccurrences());
                    System.out.println("Sending results list to client. isEmpty:" + resultsList.isEmpty());
                    outputStream.writeObject(resultsList);
                } else if (inputObject instanceof Integer) {                            //NewsObject Request
                    outputStream.writeObject(searchServer.getNewsObject((int) inputObject));
                }
            }
        } catch (EOFException | SocketException eofException) {
            System.out.println("Socket closed by exception. Client disconnected");
            try {
                socket.close();
                searchServer.userDisconnected();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Inner IOException.");
            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Outer exception");
        } finally {
            if(!socket.isClosed()){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("IOException - Closing socket in finally");
                }
            }
        }
    }
}