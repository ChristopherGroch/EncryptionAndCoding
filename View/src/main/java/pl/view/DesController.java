package pl.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import pl.model.Operations.FileOperations;
import pl.model.TripleDES.TripleDes;

import java.io.*;
import java.nio.charset.StandardCharsets;


public class DesController extends Controller {


    public Button MainSwitch;
    public Button encryptButton;
    public Button decryptButton;
    TripleDes desAlgorithm;

    String charset = "UTF-8";

    File fileToOperateOn;
    
    byte[] encryptedText;
    
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
    private TextField key1;

    @FXML
    private TextField key2;

    @FXML
    private TextField key3;

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
            desAlgorithm.setKeys(key1.getText().getBytes(charset),
                    key2.getText().getBytes(charset), key3.getText().getBytes(charset));
            if (textChoice.isDisabled()) {

                plaintext = desAlgorithm.decodeMessage(encryptedText);
                plainText.setText(new String(plaintext,charset));
            } else {

                byte[] file = FileOperations.loadBytesFromFile(fileToOperateOn.getAbsolutePath());
                if (file != null) {
                    fileAfterOperation = desAlgorithm.decodeMessage(file);
                    saveFileBtn.setVisible(true);
                }
            }

        }catch (IOException e) {
            exceptionWindow(e);
        }

    }

    @FXML
    void encrypt(ActionEvent ignoredEvent) {
        try {
            desAlgorithm.setKeys(key1.getText().getBytes(charset),
                    key2.getText().getBytes(charset), key3.getText().getBytes(charset));
            if (textChoice.isDisabled()) {

                plaintext = plainText.getText().getBytes(charset);
                encryptedText = desAlgorithm.encodeMessage(plaintext);
                cipherText.setText(new String(encryptedText,charset));
            } else {

                byte[] file = FileOperations.loadBytesFromFile(fileToOperateOn.getAbsolutePath());
                if (file != null) {
                    fileAfterOperation = desAlgorithm.encodeMessage(file);
                    saveFileBtn.setVisible(true);
                }
            }

        }catch (IOException e) {
            exceptionWindow(e);
        }


    }




    @FXML
    void readKeys(ActionEvent ignoredEvent) {
        File selectedFile = OpenFile();
        if (selectedFile != null) {

            byte[] byteArray = FileOperations.loadBytesFromFile(selectedFile.getAbsolutePath());
            String text;
            if (byteArray != null) {
                text = new String(byteArray, StandardCharsets.UTF_8);

                String[]array= text.split("\n");
                key1.setText(array[0]);
                key2.setText(array[1]);
                key3.setText(array[2]);
            }
        }

    }

    @FXML
    void saveKeys(ActionEvent ignoredEvent) {
        File file = saveFile();

        String str = String.join("\n",key1.getText(),key2.getText(),key3.getText());

        if (file != null) {
            FileOperations.saveBytesToFile(file.getAbsolutePath(),str.getBytes());

        }

    }

    @FXML
    void switchToMainWindow(ActionEvent event) {
        switchPanelTo(event, "mainWindow.fxml");

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
    void initialize(){
        desAlgorithm = new TripleDes();
        fileReadBtn.setVisible(false);
        saveFileBtn.setVisible(false);
        fileNameTextField.setVisible(false);
        fileNameTextField.setDisable(true);
    }


    public void exceptionWindow(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(e.getMessage());
        alert.setHeaderText("Error occurred");
        alert.showAndWait();
    }


}
