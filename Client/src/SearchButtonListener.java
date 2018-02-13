import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class SearchButtonListener implements ActionListener {

    private JTextField textField;
    private Client client;

    public SearchButtonListener(JTextField textField, Client client) {
        this.textField = textField;
        this.client = client;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        client.startSearching(textField.getText());
    }
}
