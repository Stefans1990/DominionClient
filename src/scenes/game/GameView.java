package scenes.game;


import com.sun.javafx.font.freetype.HBGlyphLayout;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import util.ServiceLocator;

import java.util.*;

public class GameView {

    private static final double STANDARD_INSET = 30;
    private final GameModel model;
    private ResourceBundle bundle;
    private BorderPane parent;
    private final double SQUARE_CARD_WIDTH = 100;
    private final double RECTANGLE_CARD_WIDTH = 80;
    private final double RECTANGLE_CARD_HEIGHT = 120;


    public GameView(GameModel model) {
        this.model = model;
        this.bundle = ServiceLocator.getResourceBundle();
    }

    public void createLayout() {
        parent = new BorderPane();
        parent.setLeft(createLeft());
        parent.setTop(createTop());
        parent.setCenter(createCenter());
        parent.setRight(createRight());
        parent.setBottom(createBottom());


    }

    private Node createLeft() {


        StackPane province = new StackPane();
        StackPane duchy = new StackPane();
        StackPane estate = new StackPane();
        StackPane curse = new StackPane();

        VBox victoryCardsVBox = new VBox(20);
        victoryCardsVBox.setPadding(new Insets(STANDARD_INSET));
        victoryCardsVBox.getChildren().addAll(province, duchy, estate, curse);

        for (Node node : victoryCardsVBox.getChildren()) {
            StackPane pane = (StackPane) node;
            pane.setStyle("-fx-background-color: aqua");
            pane.setPrefSize(100, 100);
        }

        StackPane gold = new StackPane();
        StackPane silver = new StackPane();
        StackPane copper = new StackPane();

        VBox coinCardsVBox = new VBox(20);

        coinCardsVBox.setPadding(new Insets(STANDARD_INSET));

        coinCardsVBox.getChildren().addAll(gold, silver, copper);

        for (Node node : coinCardsVBox.getChildren()) {
            StackPane pane = (StackPane) node;
            pane.setStyle("-fx-background-color: darkslategrey");
            pane.setPrefSize(SQUARE_CARD_WIDTH, SQUARE_CARD_WIDTH);
        }


        return new HBox(victoryCardsVBox, coinCardsVBox);
    }

    private Node createTop() {
        HBox otherPlayerHBox = new HBox();
        return otherPlayerHBox;
    }

    private Node createCenter() {
        VBox centerBox = new VBox(10);
        centerBox.setPadding(new Insets(STANDARD_INSET));

        final int actionCardsPerRow = 5;
        int numberOfActionCardRows = 2;// model.getActionCardSize() / actionCardsPerRow;

        //TODO: DELETE WHEN IMPLEMENTATION WORKS
        model.getActionCardList().put(CardName.CHANCELLOR, 10);
        model.getActionCardList().put(CardName.VILLAGE, 10);
        model.getActionCardList().put(CardName.LABORATORY, 10);
        model.getActionCardList().put(CardName.FESTIVAL, 10);
        model.getActionCardList().put(CardName.WOODCUTTER, 10);
        model.getActionCardList().put(CardName.SMITHY, 10);
        model.getActionCardList().put(CardName.COUNCILROOM, 10);
        model.getActionCardList().put(CardName.MARKET, 10);
        model.getActionCardList().put(CardName.WORKSHOP, 10);
        model.getActionCardList().put(CardName.WITCH, 10);

        Hashtable<CardName, Integer> actionCardList = model.getActionCardList();

        Set<CardName> cardNames = actionCardList.keySet();

        Iterator<CardName> iterator = cardNames.iterator();

        for (int i = 0; i < numberOfActionCardRows; i++) {
            HBox actionCardRowBox = new HBox(10);
            while (actionCardRowBox.getChildren().size() < actionCardsPerRow) {
                CardName next = iterator.next();
                if (next != null && actionCardList.get(next) != 0) {
                    StackPane stackPane = new StackPane();
                    // TODO: Get the according image for the card to add to the stackPane
                    stackPane.setMinSize(SQUARE_CARD_WIDTH, SQUARE_CARD_WIDTH);
                    stackPane.setMaxSize(SQUARE_CARD_WIDTH, SQUARE_CARD_WIDTH);
                    stackPane.setStyle("-fx-background-color: darkgreen");
                    actionCardRowBox.getChildren().add(stackPane);
                }
            }
            centerBox.getChildren().add(actionCardRowBox);
        }

        HBox yourCards = new HBox();
        VBox turnButtons = new VBox();
        Button playCards = new Button(bundle.getString("g_playCards_message"));
        Button endTurn = new Button(bundle.getString("g_endTurn_message"));
        turnButtons.getChildren().addAll(playCards, endTurn);

        Label actionAndBuys = new Label();
        return new VBox(centerBox, yourCards, actionAndBuys, turnButtons);

    }

    private Node createRight() {
        VBox chatArea = new VBox(10);
        chatArea.setPadding(new Insets(STANDARD_INSET));
        TextArea chat = new TextArea();
        chat.setMinWidth(250);
        chat.setMaxWidth(250);
        chat.setMinHeight(600);
        chat.setMaxHeight(600);


        chat.setEditable(false);

        HBox textInsert = new HBox();

        TextField chatText = new TextField();
        chatText.setMinWidth(150);

        chatText.setEditable(true);

        Button sendMessage = new Button(bundle.getString("g_send_message"));

        textInsert.getChildren().addAll(chatText, sendMessage);

        sendMessage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //TODO send message to server
            }
        });
        chatArea.getChildren().addAll(chat, textInsert);
        return chatArea;
    }


    private Node createBottom() {
        HBox bottomHBox = new HBox();
        VBox deckAndPoints = new VBox();
        HBox deckAndTray = new HBox(10);
        int winningPoints=0;//get the winningpoints
        Label points = new Label(bundle.getString("g_winning_points")+" " + winningPoints);
        StackPane trayStack = new StackPane();
        trayStack.setMinSize(SQUARE_CARD_WIDTH, SQUARE_CARD_WIDTH);
        trayStack.setMaxSize(SQUARE_CARD_WIDTH, SQUARE_CARD_WIDTH);
        trayStack.setStyle("-fx-background-color: darkslategrey");
        StackPane deck = new StackPane();
        deck.setMinSize(SQUARE_CARD_WIDTH, SQUARE_CARD_WIDTH);
        deck.setMaxSize(SQUARE_CARD_WIDTH, SQUARE_CARD_WIDTH);
        deck.setStyle("-fx-background-color: darkslategrey");
        deckAndTray.getChildren().addAll(trayStack, deck);
        deckAndPoints.getChildren().addAll(points, deckAndTray);

        HBox handCardsBox = new HBox(10);
        Pane cardOne = new Pane();
        cardOne.setStyle("-fx-background-color: darkslategrey");
        cardOne.setPrefSize(SQUARE_CARD_WIDTH, SQUARE_CARD_WIDTH);
        Pane cardTwo = new Pane();
        cardTwo.setStyle("-fx-background-color: darkslategrey");
        cardTwo.setPrefSize(SQUARE_CARD_WIDTH, SQUARE_CARD_WIDTH);
        Pane cardThree = new Pane();
        cardThree.setStyle("-fx-background-color: darkslategrey");
        cardThree.setPrefSize(SQUARE_CARD_WIDTH, SQUARE_CARD_WIDTH);
        Pane cardFour = new Pane();
        cardFour.setStyle("-fx-background-color: darkslategrey");
        cardFour.setPrefSize(SQUARE_CARD_WIDTH, SQUARE_CARD_WIDTH);
        Pane cardFive = new Pane();
        cardFive.setStyle("-fx-background-color: darkslategrey");
        cardFive.setPrefSize(SQUARE_CARD_WIDTH, SQUARE_CARD_WIDTH);
        handCardsBox.getChildren().addAll(cardOne, cardTwo, cardThree, cardFour, cardFive);


        bottomHBox.getChildren().addAll(deckAndPoints, handCardsBox);
        return bottomHBox;
    }

    public void show(Stage stage) {
        Scene scene = new Scene(parent);
        stage.setTitle("Dominion");
        stage.setScene(scene);
        stage.show();
        stage.setMaximized(true);
    }
}
