import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class GUI {
    private JFrame frame;
    private DefaultListModel<WorkerSearchResult> model = new DefaultListModel<>();
    private Client client;
    private JList<WorkerSearchResult> listPanel;
    private JTextPane textArea;
    private ListSelectionListener listSelectionListener;

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
        textArea = new JTextPane();
        JScrollPane textPanel = new JScrollPane(textArea);
        textArea.setEditable(false);
        frame.add(textPanel);

        //Create ListSelectionListener
        listSelectionListener = getNewListSelectionListener();

        //Add list click listener
        listPanel.addListSelectionListener(listSelectionListener);

        //Top Panel (Search bar + button)
        JTextField textField = new JTextField(25);
        panel.add(textField);
        JButton searchButton = new JButton("Search");
        panel.add(searchButton);
        SearchButtonListener searchButtonListener = new SearchButtonListener(textField, client);
        searchButton.addActionListener(searchButtonListener);

        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                client.closeSocket();
                System.exit(0);
            }
        });
        frame.setVisible(true);
    }

    public void updateModel(ArrayList<WorkerSearchResult> search) {
        listPanel.removeListSelectionListener(listSelectionListener);
        model = new DefaultListModel<>();
        int count = 0;
        for(WorkerSearchResult newsItem: search) {
            model.add(count, newsItem);
            count++;
        }
        listPanel.setModel(model);
        listSelectionListener = getNewListSelectionListener();
        listPanel.addListSelectionListener(listSelectionListener);
    }

    public ListSelectionListener getNewListSelectionListener(){
        return e -> {
            if(!e.getValueIsAdjusting()){
                WorkerSearchResult menuNewsItem = listPanel.getSelectedValue();
                int id = menuNewsItem.getNewsArticleID();
                client.askForNewsItem(id);
            }
        };
    }

    public void updateText(NewsObject newsObject){
        textArea.setText(newsObject.getBody());
    }


}
