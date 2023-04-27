package pl.view;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import pl.model.HuffmanCoding.Server;

import java.io.File;

public class ServerService extends Service<String> {
    private final String filepath;
    public ServerService(Button loadB,Button codeB, Button decodeB, TextField tf, String filepath){
        this.filepath = filepath;
        setOnSucceeded(workerStateEvent -> {
           File file = new File((String) workerStateEvent.getSource().getValue());
           tf.setText(file.getName());
           codeB.setDisable(false);
           decodeB.setDisable(false);
           loadB.setDisable(false);
        });
    }
    @Override
    protected Task<String> createTask(){
        return new Task<>() {
            @Override
            protected String call(){
                Server.StartReceiving(filepath);
                return filepath;
            }
        };
    }

}
