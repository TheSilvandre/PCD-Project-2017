import javax.swing.*;
import java.awt.*;

public class GUI {
    private JFrame frame;
    private SearchButtonListener searchButtonListener;

    public GUI(String windowName){
        frame = new JFrame(windowName);
        constructFrame();
    }

    private void constructFrame() {
        frame.setLayout(new BorderLayout());
        frame.setMinimumSize(new Dimension(992, 558));

        //Top Panel (Search bar + buttons)
        JPanel panel = new JPanel();
        frame.add(panel, BorderLayout.NORTH);

        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> listPanel = new JList<String>();
        JScrollPane scrollPane = new JScrollPane(listPanel);
        frame.add(scrollPane, BorderLayout.WEST);
        JTextPane textPanel = new JTextPane();
        frame.add(textPanel, BorderLayout.EAST);

        //Top Panel (Search bar + button)
        JTextField textField = new JTextField(25);
        panel.add(textField);
        JButton searchButton = new JButton("Search");
        panel.add(searchButton);
        searchButtonListener = new SearchButtonListener(textField);
        searchButton.addActionListener(searchButtonListener);


        textPanel.setEditable(false);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
