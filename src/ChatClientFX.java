import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class ChatClientFX extends Application {
    private VBox chatBox;       
    private TextField inputField;    
    private PrintWriter out;         
    private Socket socket;           
    private String username;         

    @Override
    public void start(Stage stage) {
        TextInputDialog dialog = new TextInputDialog("Student");
        dialog.setTitle("Login");
        dialog.setHeaderText("Enter your username");
        dialog.setContentText("Username:");
        username = dialog.showAndWait().orElse("Student");

        chatBox = new VBox(5);
        ScrollPane scrollPane = new ScrollPane(chatBox);
        scrollPane.setFitToWidth(true);

        inputField = new TextField();
        inputField.setPromptText("Type your message...");
        inputField.setStyle("-fx-background-color: #ffffff; -fx-border-color: #3498db; -fx-border-radius: 5; -fx-padding: 5;");

        Button sendButton = new Button("Send");       
        sendButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        
        Button photoButton = new Button("Send Photo");
        photoButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        
        Button clearButton = new Button("Clear Chat");
        clearButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");

        sendButton.setOnAction(e -> sendMessage());
        photoButton.setOnAction(e -> sendPhoto(stage));
        clearButton.setOnAction(e -> chatBox.getChildren().clear());

        HBox inputBar = new HBox(10, inputField, sendButton, photoButton, clearButton);
        inputBar.setStyle("-fx-padding: 10; -fx-background-color: #ecf0f1;");

        BorderPane root = new BorderPane();
        root.setCenter(scrollPane);
        root.setBottom(inputBar);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #2c3e50, #34495e);");

        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("💬 Chat Client - " + username);
        stage.setScene(scene);
        stage.show();

        connectToServer(); 
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
            socket = new Socket("localhost", 12823); 
            out = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        String finalMessage = message;
                        Platform.runLater(() -> handleIncoming(finalMessage));
                    }
                } catch (IOException e) {
                    Platform.runLater(() -> chatBox.getChildren().add(new Label("Connection lost.")));
                }
            }).start();
        } catch (IOException e) {
            chatBox.getChildren().add(new Label("Unable to connect to server"));
        }
    }

    private void handleIncoming(String message) {
        if (message.contains("sent a photo:")) {
            Label label = new Label(message);
            label.setStyle("-fx-text-fill: #2980b9; -fx-font-weight: bold;");
            chatBox.getChildren().add(label);

            String[] parts = message.split(": ");
            if (parts.length == 2) {
                String filename = parts[1].trim();
                File file = new File("received_" + filename);
                if (file.exists()) {
                    Image img = new Image(file.toURI().toString(), 150, 150, true, true);
                    chatBox.getChildren().add(new ImageView(img));
                }
            }
        } else {
            Label label = new Label(message);
            label.setStyle("-fx-text-fill: white;");
            chatBox.getChildren().add(label);
        }
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            out.println(username + ": " + message); 
            Label label = new Label(username + ": " + message);
            label.setStyle("-fx-text-fill: #27ae60;");
            chatBox.getChildren().add(label);
            inputField.clear();
        }
    }

    private void sendPhoto(Stage stage) {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(stage);
        if (file != null) {
            try {
                out.println("PHOTO:" + username + ":" + file.getName() + ":" + file.length());
                out.flush();

                FileInputStream fis = new FileInputStream(file);
                OutputStream os = socket.getOutputStream();
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.flush();
                fis.close();

                Label label = new Label(username + " sent a photo: " + file.getName());
                label.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                chatBox.getChildren().add(label);

                Image img = new Image(file.toURI().toString(), 150, 150, true, true);
                chatBox.getChildren().add(new ImageView(img));
            } catch (IOException e) {
                chatBox.getChildren().add(new Label("Failed to send photo."));
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
