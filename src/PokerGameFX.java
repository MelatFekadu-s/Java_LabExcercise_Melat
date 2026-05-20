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
        Label player2Label = new Label("Player 2 hand:");
        Label resultLabel = new Label("Result will appear here");
        Label scoreLabel = new Label("Scores → Player 1: 0 | Player 2: 0");

        HBox player1Cards = new HBox(10);
        HBox player2Cards = new HBox(10);

        Button dealButton = new Button("Deal Cards");
        Button newGameButton = new Button("New Game");
        Button rulesButton = new Button("Rules");
        Button exitButton = new Button("Exit");

        // Deal button logic
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

                player1Cards.getChildren().add(new Label(c1.toString()));
                player2Cards.getChildren().add(new Label(c2.toString()));
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

        // New Game button logic
        newGameButton.setOnAction(e -> {
            player1Cards.getChildren().clear();
            player2Cards.getChildren().clear();
            resultLabel.setText("Result will appear here");
        });

        // Rules button logic
        rulesButton.setOnAction(e -> {
            Alert rulesAlert = new Alert(Alert.AlertType.INFORMATION);
            rulesAlert.setTitle("Poker Game Rules");
            rulesAlert.setHeaderText("Simplified Rules");
            rulesAlert.setContentText("Each player is dealt 5 cards.\n"
                    + "The player with the highest ranked card wins.\n"
                    + "Ranks go from 2 (lowest) to Ace (highest).\n"
                   );
            rulesAlert.showAndWait();
        });

        // Exit button logic
        exitButton.setOnAction(e -> stage.close());

        // Layout
        HBox buttonBar = new HBox(10, dealButton, newGameButton, rulesButton, exitButton);
        VBox root = new VBox(15, buttonBar, player1Label, player1Cards, player2Label, player2Cards, resultLabel, scoreLabel);

        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Poker Game");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
