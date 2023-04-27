package pl.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import pl.model.HuffmanCoding.Client;
import pl.model.HuffmanCoding.DecodeHuffman;
import pl.model.HuffmanCoding.CodeHuffman;
import java.io.File;

public class HuffmanController extends Controller{



    private final CodeHuffman codeHuffman = new CodeHuffman();
    private final DecodeHuffman decodeHuffman = new DecodeHuffman();
    public Button MainWindowSwitch;
    public Button receiveButton;
    private File fileToOperateOn;
    private boolean lastOperationIsCode = true;
    private ServerService serverService;

    @FXML
    private TextField textToCodeLength;
    @FXML
    private TextField codedTextLength;
    @FXML
    private TextField IPAddress;
    @FXML
    private Button sendButton;
    @FXML
    private Button saveFileBtn;
    @FXML
    private Button codeButton;
    @FXML
    private Button decodeButton;
    @FXML
    private Button fileReadButton;

    @FXML
    private Text length1;
    @FXML
    private Text length2;
    @FXML
    private Text length3;

    @FXML
    private Text length4;
    @FXML
    private TextField fileNameTextField;

    @FXML
    private TextArea textToCode;


    @FXML
    private Button fileChoice;

    @FXML
    private TextArea codedText;

    @FXML
    private Button textChoice;


    void switchShown(boolean showText) {
        fileChoice.setDisable(!showText);
        textChoice.setDisable(showText);

        fileChoice.requestFocus();
        textChoice.requestFocus();

        length1.setVisible(showText);
        length2.setVisible(showText);
        length3.setVisible(!showText);
        length4.setVisible(!showText);

        receiveButton.setVisible(!showText);

        if(!showText){
            codedTextLength.setText("");
            textToCodeLength.setText("");
        } else {
            codedTextLength.setText(String.valueOf(codedTextLength.getText().length()));
            textToCodeLength.setText(String.valueOf(textToCode.getText().length() * 8));
        }

        textToCode.setVisible(showText);
        codedText.setVisible(showText);

        IPAddress.setVisible(!showText);
        sendButton.setVisible(!showText);

        fileReadButton.setVisible(!showText);
        saveFileBtn.setVisible(!showText);
        fileNameTextField.setVisible(!showText);

        saveFileBtn.setVisible(false);
        fileNameTextField.setText("");
        fileToOperateOn = null;
        fileNameTextField.setDisable(true);

    }
    @FXML
    void code(ActionEvent ignoredEvent){

        if(textChoice.isDisabled()){
            if(textToCode.getText().isEmpty()){
                codedText.setText("");
                codedTextLength.setText(String.valueOf(0));
                return;
            }

            codeHuffman.innitHuffmanWithString(textToCode.getText());
            codedText.setText(codeHuffman.codeToString());
            codedTextLength.setText(String.valueOf(codeHuffman.codeToString().length()));
        } else {
            if(fileToOperateOn == null){
                return;
            }

            codeHuffman.innitHuffmanWithFile(fileToOperateOn.getAbsolutePath());
            textToCodeLength.setText(String.valueOf(fileToOperateOn.length() * 8));
            codedTextLength.setText(String.valueOf(codeHuffman.codeToString().length()));
            lastOperationIsCode = true;
            saveFileBtn.setVisible(true);
        }

    }
    @FXML
    void decode(ActionEvent ignoredEvent){

        if(textChoice.isDisabled()){
            decodeHuffman.innitDecodeHuffmanString(codedText.getText());
            textToCode.setText(decodeHuffman.decodeToString());
        }else {
            if(fileToOperateOn == null){
                return;
            }
            decodeHuffman.innitDecodeHuffmanFile(fileToOperateOn.getAbsolutePath());
            textToCodeLength.setText(String.valueOf(fileToOperateOn.length() * 8));
            codedTextLength.setText(String.valueOf(decodeHuffman.decodeToString().length() * 8));
            lastOperationIsCode = false;
            saveFileBtn.setVisible(true);
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
            if (lastOperationIsCode) {
                codeHuffman.codeToFile(file.getAbsolutePath());
            } else {
                decodeHuffman.decodeToFile(file.getAbsolutePath());
            }
        }
    }
    @FXML
    void receiveFile(ActionEvent ignoredEvent) {
        if(serverService == null || !serverService.isRunning()){
            File file = choseFile();
            if(file != null) {

                fileNameTextField.setDisable(false);
                fileNameTextField.setText("Waiting for file...");
                fileToOperateOn = file;
                codeButton.setDisable(true);
                decodeButton.setDisable(true);
                fileReadButton.setDisable(true);
                serverService = new ServerService(fileReadButton,codeButton,decodeButton,fileNameTextField,file.getAbsolutePath());
                serverService.start();
                saveFileBtn.setVisible(false);

            }
        }
    }
    @FXML
    void sendCodedFile(ActionEvent ignoredEvent){
        if(!IPAddress.getText().isEmpty()) {
            File file = OpenFile();
            if (file != null) {
                Client.SendingMessage(IPAddress.getText(),file);
            }

        }
    }

    @FXML
    void initialize(){

        fileReadButton.setVisible(false);
        saveFileBtn.setVisible(false);
        length4.setVisible(false);
        length3.setVisible(false);
        fileNameTextField.setVisible(false);
        IPAddress.setVisible(false);
        sendButton.setVisible(false);
        receiveButton.setVisible(false);
        fileNameTextField.setDisable(true);
        textToCode.textProperty().addListener((observable, oldValue, newValue) -> textToCodeLength.setText(String.valueOf(textToCode.getText().length() * 8)));
    }

}
