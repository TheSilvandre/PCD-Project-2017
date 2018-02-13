public class Main {
    public static void main(String[] args) {
        SearchServer searchServer = new SearchServer();
        Client client = new Client(searchServer);
    }
}
