import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;

class Card {
    String suit;
    String rank;

    Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}

class Deck {
    private List<Card> cards = new ArrayList<>();

    Deck() {
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"2","3","4","5","6","7","8","9","10","J","Q","K","A"};
        for (String suit : suits) {
            for (String rank : ranks) {
                cards.add(new Card(suit, rank));
            }
        }
        Collections.shuffle(cards);
    }

    Card dealCard() {
        return cards.remove(0);
    }
}

public class PokerGameFX extends Application {
    private int player1Wins = 0;
    private int player2Wins = 0;

    @Override
    public void start(Stage stage) {
        Label player1Label = new Label("Player 1 hand:");
        player1Label.setStyle("-fx-font-weight: bold; -fx-text-fill: #2980b9; -fx-font-size: 14px;");
        Label player2Label = new Label("Player 2 hand:");
        player2Label.setStyle("-fx-font-weight: bold; -fx-text-fill: #c0392b; -fx-font-size: 14px;");
        Label resultLabel = new Label("Result will appear here");
        resultLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #27ae60; -fx-font-weight: bold;");
        Label scoreLabel = new Label("Scores → Player 1: 0 | Player 2: 0");
        scoreLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #8e44ad;");

        HBox player1Cards = new HBox(10);
        player1Cards.setStyle("-fx-padding: 10; -fx-background-color: #ecf0f1;");
        HBox player2Cards = new HBox(10);
        player2Cards.setStyle("-fx-padding: 10; -fx-background-color: #fdf2f2;");

        Button dealButton = new Button("Deal Cards");
        dealButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        Button newGameButton = new Button("New Game");
        newGameButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        Button rulesButton = new Button("Rules");
        rulesButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        Button exitButton = new Button("Exit");
        exitButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");

        dealButton.setOnAction(e -> {
            Deck deck = new Deck();
            List<Card> player1 = new ArrayList<>();
            List<Card> player2 = new ArrayList<>();

            player1Cards.getChildren().clear();
            player2Cards.getChildren().clear();

            for (int i = 0; i < 5; i++) {
                Card c1 = deck.dealCard();
                Card c2 = deck.dealCard();
                player1.add(c1);
                player2.add(c2);

                Label l1 = new Label(c1.toString());
                l1.setStyle("-fx-background-color: #d6eaf8; -fx-padding: 5; -fx-border-color: #2980b9;");
                Label l2 = new Label(c2.toString());
                l2.setStyle("-fx-background-color: #fadbd8; -fx-padding: 5; -fx-border-color: #c0392b;");
                player1Cards.getChildren().add(l1);
                player2Cards.getChildren().add(l2);
            }

            String[] ranks = {"2","3","4","5","6","7","8","9","10","J","Q","K","A"};
            int p1Score = player1.stream().mapToInt(c -> Arrays.asList(ranks).indexOf(c.rank)).max().getAsInt();
            int p2Score = player2.stream().mapToInt(c -> Arrays.asList(ranks).indexOf(c.rank)).max().getAsInt();

            if (p1Score > p2Score) {
                resultLabel.setText("Player 1 wins!");
                player1Wins++;
            } else if (p2Score > p1Score) {
                resultLabel.setText("Player 2 wins!");
                player2Wins++;
            } else {
                resultLabel.setText("It's a tie!");
            }

            scoreLabel.setText("Scores → Player 1: " + player1Wins + " | Player 2: " + player2Wins);
        });

        newGameButton.setOnAction(e -> {
            player1Cards.getChildren().clear();
            player2Cards.getChildren().clear();
            resultLabel.setText("Result will appear here");
        });

        rulesButton.setOnAction(e -> {
            Alert rulesAlert = new Alert(Alert.AlertType.INFORMATION);
            rulesAlert.setTitle("Poker Game Rules");
            rulesAlert.setHeaderText("Simplified Rules");
            rulesAlert.setContentText("Each player is dealt 5 cards.\n"
                    + "The player with the highest ranked card wins.\n"
                    + "Ranks go from 2 (lowest) to Ace (highest).\n");
            rulesAlert.showAndWait();
        });

        exitButton.setOnAction(e -> stage.close());

        HBox buttonBar = new HBox(10, dealButton, newGameButton, rulesButton, exitButton);
        buttonBar.setStyle("-fx-padding: 10; -fx-background-color: #bdc3c7;");

        VBox root = new VBox(15, buttonBar, player1Label, player1Cards, player2Label, player2Cards, resultLabel, scoreLabel);
        root.setStyle("-fx-padding: 15; -fx-background-color: linear-gradient(to bottom, #f5f7fa, #d6dbdf);");

        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Poker Game 🎲");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
