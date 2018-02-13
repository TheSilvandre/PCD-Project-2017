import java.io.Serializable;

/**
 * News object that is loaded into memory by the server
 */
public class NewsObject implements Serializable{

    private String title;
    private String body;
    private int id;

    public NewsObject(String title, String body, int id) {
        this.title = title;
        this.body = body;
        this.id = id;
    }

    /**
     * Title getter
     * @return Article title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Body getter
     * @return Article body
     */
    public String getBody() {
        return body;
    }

    /**
     * ID getter
     * @return Article ID
     */
    public int getID() {
        return id;
    }
}
