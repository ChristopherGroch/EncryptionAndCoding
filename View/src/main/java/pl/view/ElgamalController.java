package pl.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import pl.model.Operations.FileOperations;
import pl.model.ElGamal.ElGamal;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

public class ElgamalController extends Controller {

    public Button encryptButton;
    public Button decryptButton;

    @FXML
    void switchToMainWindow(ActionEvent event) {
        switchPanelTo(event,"mainWindow.fxml");
    }

    String charset = "UTF-8";

    ElGamal ElGamal;

    File fileToOperateOn;

    byte[] encodedText;

    byte[] plaintext;

    byte[] fileAfterOperation;



    @FXML
    private Button fileReadBtn;

    @FXML
    private Button saveFileBtn;

    @FXML
    private TextField fileNameTextField;

    @FXML
    private TextArea cipherText;


    @FXML
    private Button fileChoice;

    @FXML
    private TextField x;

    @FXML
    private TextField g;

    @FXML
    private TextField y;

    @FXML
    private TextField p;

    @FXML
    private TextArea plainText;

    @FXML
    private Button textChoice;

    void switchShown(boolean showText) {
        fileChoice.setDisable(!showText);
        textChoice.setDisable(showText);

        fileChoice.requestFocus();
        textChoice.requestFocus();


        plainText.setVisible(showText);
        cipherText.setVisible(showText);

        fileReadBtn.setVisible(!showText);
        saveFileBtn.setVisible(!showText);
        fileNameTextField.setVisible(!showText);

        saveFileBtn.setVisible(false);
        fileNameTextField.setText("");
        fileToOperateOn = null;
        fileNameTextField.setDisable(true);

    }

    @FXML
    void decrypt(ActionEvent ignoredEvent) {
        try {
            if (textChoice.isDisabled()) {

                plaintext = ElGamal.decryptMessage(encodedText);
                plainText.setText(new String(plaintext,charset));
            } else {

                byte[] file = FileOperations.loadBytesFromFile(fileToOperateOn.getAbsolutePath());

                if (file != null) {
                    fileAfterOperation = ElGamal.decryptMessage(file);
                }

                saveFileBtn.setVisible(true);
            }

        }catch (IOException e) {
            exceptionWindow(e);
        }


    }

    @FXML
    void encrypt(ActionEvent ignoredEvent) {
        try {
            if (textChoice.isDisabled()) {

                plaintext = plainText.getText().getBytes(charset);
                encodedText = ElGamal.encryptMessage(plaintext);
                cipherText.setText(new String(encodedText,charset));
            } else {

                byte[] file = FileOperations.loadBytesFromFile(fileToOperateOn.getAbsolutePath());
                if (file != null) {
                    fileAfterOperation = ElGamal.encryptMessage(file);
                }
                saveFileBtn.setVisible(true);
            }

        }catch (IOException e) {
            exceptionWindow(e);
        }


    }




    @FXML
    void readKeys(ActionEvent ignoredEvent) {
        File selectedFile = OpenFile();
        if (selectedFile != null) {
            try {

                byte[] byteArray = FileOperations.loadBytesFromFile(selectedFile.getAbsolutePath());
                String text;
                if (byteArray != null) {
                    text = new String(byteArray, charset);

                    String[]array= text.split("\n");
                    x.setText(array[0]);
                    y.setText(array[1]);
                    g.setText(array[2]);
                    p.setText(array[3]);
                    ElGamal.setKeys(new BigInteger(p.getText(), 16), new BigInteger(g.getText(), 16)
                            , new BigInteger(x.getText(), 16), new BigInteger(y.getText(), 16));
                }
            } catch (IOException e) {
                exceptionWindow(e);
            }
        }

    }

    @FXML
    void saveKeys(ActionEvent ignoredEvent) {
        File file = saveFile();

        String str = String.join("\n",x.getText(),y.getText(),g.getText(),p.getText());

        if (file != null) {
            FileOperations.saveBytesToFile(file.getAbsolutePath(),str.getBytes());

        }

    }



    @FXML
    void textClick(ActionEvent ignoredEvent) {
        switchShown(true);
    }

    @FXML
    void fileClick(ActionEvent ignoredEvent) {
        switchShown(false);
    }

    @FXML
    void fileRead(ActionEvent ignoredEvent) {
        fileToOperateOn = OpenFile();
        if (fileToOperateOn != null)
            fileNameTextField.setText(fileToOperateOn.getName());
        saveFileBtn.setVisible(false);
        fileNameTextField.setDisable(false);
    }

    @FXML
    void saveFile(ActionEvent ignoredEvent) {
        File file = saveFile();
        if (file != null) {
            FileOperations.saveBytesToFile(file.getAbsolutePath(), fileAfterOperation);

        }
    }

    @FXML
    void confirmKeys(){
        try {
            ElGamal.setKeys(new BigInteger(p.getText(),16),new BigInteger(g.getText(),16)
                    ,new BigInteger(x.getText(),16),new BigInteger(y.getText(),16));
        }catch (IOException e){
            exceptionWindow(e);
        }

    }

    @FXML
    void generateKeys(){
        ElGamal.generateKeys();
        p.setText(ElGamal.getP());
        g.setText(ElGamal.getG());
        x.setText(ElGamal.getX());
        y.setText(ElGamal.getY());
    }

    @FXML
    void initialize(){
        fileReadBtn.setVisible(false);
        saveFileBtn.setVisible(false);
        fileNameTextField.setVisible(false);
        fileNameTextField.setDisable(true);

        ElGamal = new ElGamal();
        p.setText(ElGamal.getP());
        g.setText(ElGamal.getG());
        x.setText(ElGamal.getX());
        y.setText(ElGamal.getY());
    }


    public void exceptionWindow(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(e.getMessage());
        alert.setHeaderText("Error occurred");
        alert.showAndWait();
    }


}
