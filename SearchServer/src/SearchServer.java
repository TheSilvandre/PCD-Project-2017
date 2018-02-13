import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SearchServer {
    private int connectedClients = 0;
    private long currentClientID = 0;
    private SearchMotor searchMotor;
    private ArrayList<ClientManager> clients = new ArrayList<>();

    public SearchServer() {
        try {
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() throws IOException {
        ServerSocket serverClientSocket;
        ServerSocket serverWorkerSocket;

        List<NewsObject> newsObjects = loadNewsObjects();
        searchMotor = new SearchMotor(newsObjects);

        serverClientSocket = new ServerSocket(Ports.CLIENT.getPort());
        System.out.println("Accepting clients on port: " + Ports.CLIENT.getPort() + ".");
        serverWorkerSocket = new ServerSocket(Ports.WORKER.getPort());
        System.out.println("Accepting workers on port: " + Ports.WORKER.getPort() + ".");

        ConnectionManager clientConnectionManager = new ConnectionManager(serverClientSocket, this);
        ConnectionManager workerConnectionManager = new ConnectionManager(serverWorkerSocket, this);
        clientConnectionManager.start();
        workerConnectionManager.start();
    }

    public NewsObject getNewsObject(int inputObject) {
        return searchMotor.getNewsObject(inputObject);
    }

    public void userDisconnected() {
        connectedClients--;
    }

    private List<NewsObject> loadNewsObjects() {
        List<NewsObject> list = new ArrayList<>();
        File dir = new File("news");
        File[] directoryListing = dir.listFiles();
        if(directoryListing != null) {
            int count = 0;
            for(File f: directoryListing){
                Scanner scanner = null;
                try {
                    scanner = new Scanner(f);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }

                String title = "";
                String body = "";

                if(scanner.hasNext()) {
                    title = scanner.nextLine();
                }
                if(scanner.hasNext()) {
                    body = scanner.nextLine();
                }

                list.add(new NewsObject(title, body, count));

                count++;
            }

        } else {
            System.out.println("Path not found.");
        }
        return list;
    }

    public void startClientManager(Socket socket) {
        ClientManager manager = new ClientManager(socket, this, currentClientID++);
        clients.add(manager);
        manager.start();
        connectedClients++;
    }

    public void startWorkerManager(Socket socket) {
        WorkerManager manager = new WorkerManager(socket, this, searchMotor);
        searchMotor.registerWorker(manager);
        manager.start();
    }

    public SearchMotor getSearchMotor() {
        return searchMotor;
    }
}
