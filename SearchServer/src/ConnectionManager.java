import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionManager extends Thread{

    private ServerSocket serverSocket;
    private SearchServer searchServer;

    public ConnectionManager(ServerSocket serverSocket, SearchServer searchServer) {
        this.serverSocket = serverSocket;
        this.searchServer = searchServer;
    }

    @Override
    public void run() {
        Socket socket = null;
        while(true){
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(socket == null)
                continue;
            if(serverSocket.getLocalPort() == Ports.CLIENT.getPort()) {            //Client connected
                searchServer.startClientManager(socket);
            } else if (serverSocket.getLocalPort() == Ports.WORKER.getPort()) {    //Worker connected
                searchServer.startWorkerManager(socket);
            }
        }
    }
}
