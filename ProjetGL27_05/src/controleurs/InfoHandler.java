package controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.awt.Desktop;
import java.net.URI;

public class InfoHandler implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent event) {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/BaptistePlautA/ProjetGL3"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
