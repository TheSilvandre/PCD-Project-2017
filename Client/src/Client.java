import java.util.LinkedList;

public class Client {

    private final SearchServer searchServer;
    private GUI gui;

    public Client(SearchServer searchServer) {
        openGUI();
        this.searchServer = searchServer;
    }

    private void openGUI(){
        this.gui = new GUI("Client", this);
    }

    public void startSearching(String text) {
        LinkedList<MenuNewsItem> list = searchServer.search(text);
        gui.updateModel(list);
    }

    public NewsObject askServerForNews(int id){
        return searchServer.getNews(id);
    }
}
