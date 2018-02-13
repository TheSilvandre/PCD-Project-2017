import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.LinkedList;

public class GUI {
    private JFrame frame;
    private DefaultListModel<MenuNewsItem> model = new DefaultListModel<>();
    private Client client;
    private JList<MenuNewsItem> listPanel;

    public GUI(String windowName, Client client){
        frame = new JFrame(windowName);
        this.client = client;
        constructFrame();
    }

    private void constructFrame() {
        frame.setLayout(new BorderLayout());
        frame.setMinimumSize(new Dimension(992, 558));

        //Top Panel (Search bar + buttons)
        JPanel panel = new JPanel();
        frame.add(panel, BorderLayout.NORTH);

        listPanel = new JList<>(model);
        JScrollPane scrollPane = new JScrollPane(listPanel);
        frame.add(scrollPane, BorderLayout.WEST);
        JTextPane textArea = new JTextPane();
        JScrollPane textPanel = new JScrollPane(textArea);
        textArea.setEditable(false);
        frame.add(textPanel);

        //Add list click listener
        listPanel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()){
                    textArea.setText(client.askServerForNews(model.get(e.getLastIndex()).getId()).getBody());
                    e.getLastIndex();
                }
            }
        });

        //Top Panel (Search bar + button)
        JTextField textField = new JTextField(25);
        panel.add(textField);
        JButton searchButton = new JButton("Search");
        panel.add(searchButton);
        SearchButtonListener searchButtonListener = new SearchButtonListener(textField, client);
        searchButton.addActionListener(searchButtonListener);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void updateModel(LinkedList<MenuNewsItem> search) {
        model.clear();
        int count = 0;
        for(MenuNewsItem newsItem: search) {
            model.add(count, newsItem);
            count++;
        }
//        listPanel.setModel(model);
    }
}
