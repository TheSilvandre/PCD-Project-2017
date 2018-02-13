public class NewsObject {
    private String title;
    private String body;
    private int id;

    public NewsObject(String title, String body, int id) {
        this.title = title;
        this.body = body;
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getBody() {
        return this.body;
    }

    public int getID() {
        return this.id;
    }
}
