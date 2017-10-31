import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Scanner;

public class SearchButtonListener implements ActionListener {

    JTextField textField;
    public SearchButtonListener(JTextField textField) {
        this.textField = textField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File dir = new File("news");
        File[] directoryListing = dir.listFiles();
        if(directoryListing != null) {
            for(File f: directoryListing) {
                Scanner scanner = new Scanner(f.getAbsolutePath());

                String title = scanner.nextLine();
                String body = scanner.nextLine();

                System.out.println(NewsObjectParser.countOccurences(textField.getText(), new NewsObject(title, body)));
            }
        } else {
            System.out.println("Path not found.");
        }

    }
}
