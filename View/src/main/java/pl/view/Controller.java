package pl.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Controller {

    protected Stage stage;

    private Parent parent;

    protected FileChooser chooser;

    void switchPanelTo(ActionEvent event, String fileName){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource(fileName));
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    File OpenFile() {
        chooser = new FileChooser();
        chooser.setTitle("Open Resource File");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        return chooser.showOpenDialog(stage);
    }


    File saveFile() {
        chooser = new FileChooser();
        chooser.setTitle("Save File in resources");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));

        return chooser.showSaveDialog(stage);
    }
    File choseFile() {
        chooser = new FileChooser();
        chooser.setTitle("Recieve File");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));

        return chooser.showSaveDialog(stage);
    }

}
