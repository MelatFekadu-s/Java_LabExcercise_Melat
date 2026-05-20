import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class ChatClientFX extends Application {
    private TextArea chatArea;       
    private TextField inputField;    
    private PrintWriter out;         
    private Socket socket;           
    private String username;         

    @Override
    public void start(Stage stage) {
        // ask for username when app starts
        TextInputDialog dialog = new TextInputDialog("Student");
        dialog.setTitle("Login");
        dialog.setHeaderText("Enter your username");
        dialog.setContentText("Username:");
        username = dialog.showAndWait().orElse("Student");

        chatArea = new TextArea();
        chatArea.setEditable(false); // cannot type directly here
        chatArea.setWrapText(true);

        inputField = new TextField();
        inputField.setPromptText("Type your message...");

        Button sendButton = new Button("Send");       
        Button photoButton = new Button("Send Photo");
        Button clearButton = new Button("Clear Chat");

        sendButton.setOnAction(e -> sendMessage());
        photoButton.setOnAction(e -> sendPhoto(stage));
        clearButton.setOnAction(e -> chatArea.clear());

        // put input + buttons at bottom
        HBox inputBar = new HBox(10, inputField, sendButton, photoButton, clearButton);
        BorderPane root = new BorderPane();
        root.setCenter(chatArea);
        root.setBottom(inputBar);

        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Chat Client - " + username);
        stage.setScene(scene);
        stage.show();

        connectToServer(); // try to connect

        // close connection when app closes
        stage.setOnCloseRequest(e -> {
            try {
                if (out != null) out.close();
                if (socket != null && !socket.isClosed()) socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 12345); // connect to server
            out = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // thread to listen for server messages
            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        chatArea.appendText(message + "\n");
                    }
                } catch (IOException e) {
                    chatArea.appendText("Connection lost.\n");
                }
            }).start();
        } catch (IOException e) {
            chatArea.appendText("Unable to connect to server\n");
        }
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            out.println(username + ": " + message); 
            chatArea.appendText("Me: " + message + "\n"); 
            inputField.clear();
        }
    }

    private void sendPhoto(Stage stage) {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(stage);
        if (file != null) {
            chatArea.appendText("Me sent a photo: " + file.getName() + "\n");
            out.println(username + " sent a photo: " + file.getName());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
