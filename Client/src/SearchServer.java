import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SearchServer {

    private LinkedList<NewsObject> newsList = new LinkedList<>();

    public SearchServer() {
        loadFiles();
    }

    //TODO Optimize this method
    private void loadFiles() {
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

                newsList.add(new NewsObject(title, body, count));

                count++;
            }

        } else {
            System.out.println("Path not found.");
        }
    }

    public LinkedList<MenuNewsItem> search(String input){
        LinkedList<MenuNewsItem> list = new LinkedList<>();
        for(NewsObject newsObject: newsList){
            if(newsObject.getTitle().toLowerCase().contains(input.toLowerCase()) || newsObject.getBody().toLowerCase().contains(input.toLowerCase())){
                list.add(new MenuNewsItem(newsObject.getTitle(), NewsObjectParser.countOccurrences(input, newsObject), newsObject.getID()));
            }
        }
        list.sort((o1, o2) -> o2.getOccurrences() - o1.getOccurrences());
        return list;
    }

    public NewsObject getNews(int id) {
        return newsList.get(id);
    }
}