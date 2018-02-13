import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchButtonListener implements ActionListener {

    private JTextField textField;
    private Client client;

    public SearchButtonListener(JTextField textField, Client client) {
        this.textField = textField;
        this.client = client;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        client.sendSearchRequest(textField.getText());
    }
}
