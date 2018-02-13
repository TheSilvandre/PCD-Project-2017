import javax.swing.*;
import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {

    private final int SERVER_PORT;
    private final String SERVER_NAME;
    ConfigReader configReader = new ConfigReader("config.txt");

    private Socket socket;
    private ObjectOutputStream out;

    private GUI gui;

    public Client() {

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

        openGUI();

    }

    private void connect() throws IOException{
        System.out.println("Attempting to connect to server on: " + SERVER_NAME + ":" + SERVER_PORT);
        InetAddress address = InetAddress.getByName(SERVER_NAME);
        try {
            socket = new Socket(address, SERVER_PORT);
        } catch (ConnectException e) {
            JOptionPane.showMessageDialog(null, "Connection refused! Maybe the server isn't running?");
            System.exit(0);
        }

        InfoStream infoStream = new InfoStream(socket, this); //InfoStream to receive information (so the main thread won't lock up while receiving.)
        infoStream.start();
        out = new ObjectOutputStream(socket.getOutputStream());
    }

    private void openGUI(){
        gui = new GUI("Client", this);
    }

    public void sendSearchRequest(String input){
        try {
            out.writeObject(input);
        } catch (IOException e) {
            System.out.println("IOException: Perhaps you haven't started the server?");
        }
    }

    public void updateGUI(ArrayList<WorkerSearchResult> list){
        gui.updateModel(list);
    }

    public void askForNewsItem(int id) {
        try {
            out.writeObject(id);
        } catch (IOException e) {
            System.out.println("IOException: Perhaps you haven't started the server?");
        }
    }

    public GUI getGui() {
        return gui;
    }

    public void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}