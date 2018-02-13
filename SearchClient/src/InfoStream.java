import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class InfoStream extends Thread{
    private Socket socket;
    private ObjectInputStream inputStream;
    private Client client;

    public InfoStream(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
    }

    @Override
    public void run() {
        Object inputObject;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            while (true) {
                inputObject = inputStream.readObject();
                System.out.println(inputObject.getClass().toString());
                if (inputObject instanceof ArrayList) {               //If inputObject instanceof ArrayList then it's receiving the searched items.
                    ArrayList<WorkerSearchResult> inputList = (ArrayList<WorkerSearchResult>) inputObject;
                    client.updateGUI(inputList);
                } else if (inputObject instanceof NewsObject) {       //If inputObject instanceof NewsObject then it's receiving the item with the body.
                    NewsObject newsObject = (NewsObject) inputObject;
                    client.getGui().updateText(newsObject);
                }
            }
        } catch (SocketException socketException) {
            System.out.println("Socket closed. Closing thread.");
            return;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
