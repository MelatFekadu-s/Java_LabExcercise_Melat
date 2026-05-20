import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentNotepadFX extends Application {
    private TextArea textArea;       
    private TextField searchField;   
    private Button searchButton;     
    private FileChooser fileChooser; 

    @Override
    public void start(Stage stage) {
        stage.setTitle("Student Notepad (JavaFX)");

        textArea = new TextArea();
        textArea.setWrapText(true);

        searchField = new TextField();
        searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");

        ToolBar searchBar = new ToolBar(new Label("Find:"), searchField, searchButton);

        fileChooser = new FileChooser();

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem openItem = new MenuItem("Open");
        openItem.setStyle("-fx-text-fill: #2980b9;");
        MenuItem saveItem = new MenuItem("Save");
        saveItem.setStyle("-fx-text-fill: #27ae60;");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setStyle("-fx-text-fill: #e74c3c;");

        openItem.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        saveItem.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        exitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));

        fileMenu.getItems().addAll(openItem, saveItem, new SeparatorMenuItem(), exitItem);
        menuBar.getMenus().add(fileMenu);

        openItem.setOnAction(e -> openFile(stage));
        saveItem.setOnAction(e -> saveFile(stage));
        exitItem.setOnAction(e -> stage.close());
        searchButton.setOnAction(e -> showHighlightPreview(searchField.getText()));

        BorderPane root = new BorderPane();
        root.setTop(new VBox(menuBar, searchBar));
        root.setCenter(textArea);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void openFile(Stage stage) {
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                textArea.clear();
                String line;
                while ((line = reader.readLine()) != null) {
                    textArea.appendText(line + "\n");
                }
            } catch (IOException ex) {
                showError("Error opening file: " + ex.getMessage());
            }
        }
    }

    private void saveFile(Stage stage) {
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(textArea.getText());
            } catch (IOException ex) {
                showError("Error saving file: " + ex.getMessage());
            }
        }
    }

    private void showHighlightPreview(String word) {
        String content = textArea.getText();
        TextFlow textFlow = new TextFlow();

        if (word == null || word.isEmpty()) {
            textFlow.getChildren().add(new Text(content));
        } else {
            Pattern pattern = Pattern.compile(Pattern.quote(word), Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(content);

            int lastEnd = 0;
            while (matcher.find()) {
                if (matcher.start() > lastEnd) {
                    textFlow.getChildren().add(new Text(content.substring(lastEnd, matcher.start())));
                }

                Label highlight = new Label(content.substring(matcher.start(), matcher.end()));
                highlight.setStyle("-fx-background-color: yellow; -fx-text-fill: black;");
                textFlow.getChildren().add(highlight);

                lastEnd = matcher.end();
            }

            if (lastEnd < content.length()) {
                textFlow.getChildren().add(new Text(content.substring(lastEnd)));
            }
        }

        Stage previewStage = new Stage();
        previewStage.setTitle("Search Preview");
        previewStage.setScene(new Scene(new ScrollPane(textFlow), 600, 400));
        previewStage.show();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
