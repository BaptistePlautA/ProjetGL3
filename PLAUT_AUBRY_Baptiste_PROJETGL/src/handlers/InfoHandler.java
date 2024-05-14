package handlers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.awt.Desktop;
import java.net.URI;

public class InfoHandler implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent event) {
        try {
            Desktop.getDesktop().browse(new URI("https://www.google.com"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
