package pl.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainWindowController extends Controller{
    public Button ElGamal;
    public Button tripleDES;
    public Button Huffman;

    @FXML
    void goToTripleDES(ActionEvent event){
        switchPanelTo(event,"des.fxml");
    }
    @FXML
    void goToElGamal(ActionEvent event){
        switchPanelTo(event,"elgamal.fxml");
    }
    @FXML
    void goToHuffman(ActionEvent event){
        switchPanelTo(event,"huffman.fxml");
    }
}
