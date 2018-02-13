public class MenuNewsItem {
    private String title;
    private int occurrences;
    private int id;

    public MenuNewsItem(String title, int occurrences, int id) {
        this.title = title;
        this.occurrences = occurrences;
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public int getOccurrences() {
        return this.occurrences;
    }

    public int getId() {
        return this.id;
    }

    public String toString() {
        return this.occurrences + " - " + this.title;
    }
}
