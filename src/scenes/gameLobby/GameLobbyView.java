package scenes.gameLobby;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import util.ServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import Handlers.ServerChatMessageHandler;
import Handlers.GameJoinGameMessageHandler;
import Handlers.ServerNewGameMessageHandler;

public class GameLobbyView {

    private final GameLobbyModel model;
    private ResourceBundle bundle;
    protected static ArrayList<Label> topLabels = new ArrayList<>();
    ListView<String> gameListView;
    private HBox parent;
    protected TextArea chat;
    protected static ArrayList<Label> topPlayed = new ArrayList<>();
    protected static ArrayList<Label> topWon = new ArrayList<>();
    protected static ArrayList<Label> topHighScore = new ArrayList<>();

    private static SimpleBooleanProperty isGameStarted;
    // @stefan sets up the view by instantiating the logic from the model and sets the isgamestarted boolean to false
    public GameLobbyView(GameLobbyModel model) {
        this.model = model;
        this.isGameStarted = new SimpleBooleanProperty(false);
    }
    // @stefan creates the layout, the parent is an HBOX with 2 VBOXes in it, it also instantiates the resourcebundle
    public void createLayout() {
        bundle = ServiceLocator.getResourceBundle();
        parent = new HBox(30);
        parent.setAlignment(Pos.CENTER);
        parent.setPadding(new Insets(30, 50, 30, 50));
        VBox left = createLeft();
        VBox right = createRight();
        parent.getChildren().addAll(left, right);
    }
    // @stefan creates the left VBOX with the gamelist and the options to create a game
    private VBox createLeft() {
        VBox left = new VBox(10);
        VBox topLeft = createGameList();
        VBox bottomLeft = createGame();
        left.getChildren().addAll(topLeft, bottomLeft);
        return left;
    }
    // @stefan shows the gamelist to the user with a button to join a game
    private VBox createGameList() {
        VBox gameList = new VBox();
        Label gameListLabel = new Label(bundle.getString("gl_gameListLabel"));
        gameListView = new ListView<>();
        gameList.setMaxHeight(200);


        gameListView.getItems().addAll();
        Button joinButton = new Button(bundle.getString("gl_joinButton"));

        joinButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GameJoinGameMessageHandler joinHandler = new GameJoinGameMessageHandler();
                String joinedGame = gameListView.getSelectionModel().getSelectedItem();
                joinHandler.write(joinedGame);
            }
        });

        gameList.getChildren().addAll(gameListLabel, gameListView, joinButton);
        return gameList;
    }
    // @stefan in this VBOX are the gameoptions shown to start a new game with two or more players and other options
    private VBox createGame() {
        VBox createGameBox = new VBox(10);
        HBox gameOptions = new HBox(10);
        VBox leftOptions = new VBox(10);
        VBox rightOptions = new VBox(10);


        Label newGame = new Label(bundle.getString("gl_gameoptions_title"));
        newGame.setFont(new Font("Arial", 16));
        RadioButton twoPlayer = new RadioButton(bundle.getString("gl_2player"));
        twoPlayer.setSelected(true);
        RadioButton threePlayer = new RadioButton(bundle.getString("gl_3player"));
        RadioButton fourPlayer = new RadioButton(bundle.getString("gl_4player"));
        ToggleGroup playerRadioToggleGroup = new ToggleGroup();
        playerRadioToggleGroup.getToggles().addAll(twoPlayer, threePlayer, fourPlayer);

        leftOptions.getChildren().addAll(newGame, twoPlayer, threePlayer,fourPlayer);


        Label actionCards = new Label(bundle.getString("gl_actioncards"));
        RadioButton amountFive = new RadioButton(bundle.getString("gl_amountFive"));
        amountFive.setSelected(true);
        RadioButton amountTen = new RadioButton(bundle.getString("gl_amountTen"));
        ToggleGroup cardOptionsToggleGroup = new ToggleGroup();
        cardOptionsToggleGroup.getToggles().addAll(amountFive, amountTen);

        Label gameNameLabel = new Label(bundle.getString("gl_gameNameLabel"));
        TextField gameNameTextField = new TextField();

        Button createGameButton = new Button(bundle.getString("gl_create_game"));

        createGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ServerNewGameMessageHandler newGameHandler = new ServerNewGameMessageHandler();
                String actionCards = null;
                String maxPlayers = null;
                if (amountFive.isSelected()) {
                    actionCards = "5";
                } else if (amountTen.isSelected()) {
                    actionCards = "10";
                }
                if (twoPlayer.isSelected()) {
                    maxPlayers = "2";
                } else if(threePlayer.isSelected()){
                    maxPlayers = "3";
                }else if(fourPlayer.isSelected()){
                    maxPlayers="4";
                }
                String gameName = gameNameTextField.getText();
                String message = gameName + "@" + actionCards + "@" + maxPlayers;
                newGameHandler.write(message);
            }
        });


        rightOptions.getChildren().addAll( actionCards, amountFive, amountTen);

        gameOptions.getChildren().addAll(leftOptions, rightOptions);
        createGameBox.getChildren().addAll(gameOptions, gameNameLabel, gameNameTextField, createGameButton
        );
        return createGameBox;
    }

    // @stefan  creates the VBOX with the topfive and the chat in it
    private VBox createRight() {
        VBox right = new VBox(10);
        VBox topRight = createTopFive();
        VBox bottomRight = createChatArea();
        right.getChildren().addAll(topRight, bottomRight);
        return right;
    }
    // @stefan creates the list with the top five players
    private VBox createTopFive() {
        VBox topFive = new VBox(5);
        HBox topFiveLabels = new HBox(10);
        HBox topFiveOne = new HBox(10);
        HBox topFiveTwo = new HBox(10);
        HBox topFiveThree = new HBox(10);
        HBox topFiveFour = new HBox(10);
        HBox topFiveFive = new HBox(10);


        Label title = new Label(bundle.getString("gl_topfive_title"));

        topFiveLabels.getChildren().addAll(title/*,gamesPlayed, gamesWon, gamesHighScore*/);
        topLabels = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            topLabels.add(new Label(""));
            topPlayed.add(new Label(""));
            topWon.add(new Label(""));
            topHighScore.add(new Label(""));
        }

        topFive.getChildren().addAll(topFiveLabels, topLabels.get(0), topLabels.get(1), topLabels.get(2), topLabels.get(3), topLabels.get(4));
        return topFive;
    }
    // @stefan creates the chat area with the button to send messages
    private VBox createChatArea() {
        VBox chatArea = new VBox(10);

        chat = new TextArea();
        chat.setMinWidth(250);
        chat.setMaxWidth(250);
        chat.setEditable(false);

        HBox textInsert = new HBox();

        TextField chatText = new TextField();
        chatText.setMinWidth(150);

        chatText.setEditable(true);

        Button sendMessage = new Button(bundle.getString("gl_send_message"));

        textInsert.getChildren().addAll(chatText, sendMessage);

        sendMessage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ServerChatMessageHandler chatHandler = new ServerChatMessageHandler();
                chatHandler.write(chatText.getText());
            }
        });

        chatArea.getChildren().addAll(chat, textInsert);
        return chatArea;

    }

    // @stefan shows the stage and inherits the css stylesheet
    public void show(Stage stage) {
        Scene scene = new Scene(parent);
        scene.getStylesheets().add("scenes/gameLobby/gameLobbyStyleSheet.css");
        stage.setTitle("Dominion");
        stage.setScene(scene);
        stage.show();
    }

}
