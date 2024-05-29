package controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.awt.Desktop;

import java.net.URI;

/**
 * Classe InfoHandler
 * @author Groupe 3 
 * @version 1.0
 * @date 29 mai 2024
 *
 */
public class InfoHandler implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent event) {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/BaptistePlautA/ProjetGL3/tree/main"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
