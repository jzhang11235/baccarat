package com.github.javabada.baccarat.game;

import com.github.javabada.baccarat.card.Card;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.text.NumberFormat;
import java.util.Locale;

public class GameController {

    private Game game;
    private Player player;
    private Coup coup;

    @FXML private Label messageLabel;
    @FXML private Label balanceNumberLabel;
    @FXML private Button actionButton;
    @FXML private Button clearButton;
    @FXML private Button tieButton;
    @FXML private Button bankerButton;
    @FXML private Button playerButton;
    @FXML private ToggleGroup chipToggleGroup;

    @FXML private ImageView playerCardView1;
    @FXML private ImageView playerCardView2;
    @FXML private ImageView playerCardView3;
    @FXML private ImageView bankerCardView1;
    @FXML private ImageView bankerCardView2;
    @FXML private ImageView bankerCardView3;

    @FXML
    private void initialize() {
        game = new Game();
        player = new Player("10000");
        coup = new Coup(game.getShoe());

        balanceNumberLabel.setText(formatCurrency(player.getBalance()));
        actionButton.setText("Deal");
        clearButton.setDisable(true);
        tieButton.setText("Tie");
        bankerButton.setText("Banker");
        playerButton.setText("Player");
    }

    @FXML
    private void handlePlaceWagerButtonAction(ActionEvent event) {
        if (chipToggleGroup.getSelectedToggle() == null) {
            messageLabel.setText("No chips selected");
            return;
        }

        messageLabel.setText("");
        clearButton.setDisable(false);

        String wagerAmount = chipToggleGroup.getSelectedToggle().getUserData().toString();
        boolean wagerPlaced;

        if (event.getSource() == tieButton) {
            wagerPlaced = player.placeWager(Outcome.TIE, wagerAmount);
            if (wagerPlaced) {
                tieButton.setText("Tie\n" + formatCurrency(player.checkWager(Outcome.TIE)));
            }
        }
        else if (event.getSource() == bankerButton) {
            wagerPlaced = player.placeWager(Outcome.BANKER, wagerAmount);
            if (wagerPlaced) {
                bankerButton.setText("Banker\n" + formatCurrency(player.checkWager(Outcome.BANKER)));
            }
        }
        else {
            wagerPlaced = player.placeWager(Outcome.PLAYER, wagerAmount);
            if (wagerPlaced) {
                playerButton.setText("Player\n" + formatCurrency(player.checkWager(Outcome.PLAYER)));
            }
        }

        if (!wagerPlaced) {
            messageLabel.setText("Not enough money");
        }

        balanceNumberLabel.setText(formatCurrency(player.getBalance()));
    }

    @FXML
    private void handleActionButtonAction(ActionEvent event) {
        if (coup == null || coup.isFinished()) {
            messageLabel.setText("");
            actionButton.setText("Deal");
            tieButton.setDisable(false);
            bankerButton.setDisable(false);
            playerButton.setDisable(false);
            clearCards();

            player.settleWagers(coup.getWinner());
            balanceNumberLabel.setText(formatCurrency(player.getBalance()));
            tieButton.setText("Tie");
            bankerButton.setText("Banker");
            playerButton.setText("Player");

            coup = new Coup(game.getShoe());
        }
        else {
            clearButton.setDisable(true);
            tieButton.setDisable(true);
            bankerButton.setDisable(true);
            playerButton.setDisable(true);

            Card card = coup.deal();
            displayCard(card);

            if (coup.isFinished()) {
                actionButton.setText("Continue");

                if (coup.getWinner() == Outcome.PLAYER) {
                    messageLabel.setText("Player wins");
                }
                else if (coup.getWinner() == Outcome.BANKER) {
                    messageLabel.setText("Banker wins");
                }
                else {
                    messageLabel.setText("Tie");
                }
            }
        }
    }

    @FXML
    private void handleClearButtonAction(ActionEvent event) {
        player.clearWagers();
        messageLabel.setText("");
        balanceNumberLabel.setText(formatCurrency(player.getBalance()));
        clearButton.setDisable(true);
        tieButton.setText("Tie");
        bankerButton.setText("Banker");
        playerButton.setText("Player");
    }

    private String formatCurrency(Object o) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(o);
    }

    private void displayCard(Card card) {
        String cardPath = "/images/cards/" + card.toString().replace(" ", "_") + ".png";
        Image cardImage = new Image(cardPath);

        switch (coup.getCardNumber()) {
            case 1:
                playerCardView1.setImage(cardImage);
                break;
            case 2:
                bankerCardView1.setImage(cardImage);
                break;
            case 3:
                playerCardView2.setImage(cardImage);
                break;
            case 4:
                bankerCardView2.setImage(cardImage);
                break;
            case 5:
                if (coup.getCurrentHand() == Coup.Hand.PLAYER) {
                    playerCardView3.setImage(cardImage);
                }
                else {
                    bankerCardView3.setImage(cardImage);
                }
                break;
            case 6:
                bankerCardView3.setImage(cardImage);
                break;
        }
    }

    private void clearCards() {
        playerCardView1.setImage(null);
        playerCardView2.setImage(null);
        playerCardView3.setImage(null);
        bankerCardView1.setImage(null);
        bankerCardView2.setImage(null);
        bankerCardView3.setImage(null);
    }

}