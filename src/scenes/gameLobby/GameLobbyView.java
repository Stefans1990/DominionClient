package scenes.gameLobby;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import util.ServiceLocator;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Handlers.ServerChatMessageHandler;
import Handlers.ServerJoinGameMessageHandler;
import Handlers.ServerNewGameMessageHandler;

public class GameLobbyView {

    private final GameLobbyModel model;
    private ResourceBundle bundle;
    private ArrayList<Label> topLabels;
    private HBox parent;
    protected TextArea chat;

    private SimpleBooleanProperty isGameStarted;

    public GameLobbyView(GameLobbyModel model) {
        this.model = model;
        this.isGameStarted = new SimpleBooleanProperty(false);
    }

    public void createLayout() {
        bundle = ServiceLocator.getResourceBundle();
        parent = new HBox(30);
        parent.setAlignment(Pos.CENTER);
        parent.setPadding(new Insets(30,50,30,50));
        VBox left = createLeft();
        VBox right = createRight();
        parent.getChildren().addAll(left, right);
    }

    private VBox createLeft() {
        VBox left = new VBox(10);
        VBox topLeft = createGameList();
        VBox bottomLeft = createGame();
        left.getChildren().addAll(topLeft, bottomLeft);
        return left;
    }

    private VBox createGameList() {
        VBox gameList = new VBox();
        Label gameListLabel = new Label(bundle.getString("gl_gameListLabel"));
        gameListLabel.setFont(new Font ("Arial", 16));
        ListView<String> gameListView = new ListView<>();
        gameList.setMaxHeight(200);


        gameListView.getItems().addAll("game1", "game2","game3","game4","game5" );
        Button joinButton = new Button(bundle.getString("gl_joinButton"));
        //TODO Button action
        // TODO remove later
        joinButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ServerJoinGameMessageHandler joinHandler= new ServerJoinGameMessageHandler();
                String joinedGame = gameListView.getSelectionModel().getSelectedItem();
                joinHandler.write(joinedGame, true);
            	
            	
            	isGameStartedProperty().setValue(true);
                isGameStartedProperty().setValue(false);
            }
        });

        gameList.getChildren().addAll(gameListLabel, gameListView, joinButton);
        return gameList;
    }

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
        RadioButton againstAi = new RadioButton(bundle.getString("gl_againstAI"));
        ToggleGroup playerRadioToggleGroup = new ToggleGroup();
        playerRadioToggleGroup.getToggles().addAll(twoPlayer, threePlayer,againstAi);

        leftOptions.getChildren().addAll(newGame, twoPlayer, threePlayer, againstAi);


        Label winCondition = new Label(bundle.getString("gl_wincondition_title"));
        winCondition.setFont(new Font("Arial", 16));
        RadioButton shortGame = new RadioButton(bundle.getString(("gl_shortgame")));
        shortGame.setSelected(true);
        RadioButton longGame = new RadioButton(bundle.getString("gl_longgame"));
        ToggleGroup gameOptionsToggleGroup = new ToggleGroup();
        gameOptionsToggleGroup.getToggles().addAll(shortGame, longGame);

        Label actionCards = new Label(bundle.getString("gl_actioncards"));
        actionCards.setFont(new Font("Arial", 16));
        RadioButton amountFive = new RadioButton(bundle.getString("gl_amountFive"));
        amountFive.setSelected(true);
        RadioButton amountTen = new RadioButton(bundle.getString("gl_amountTen"));
        ToggleGroup cardOptionsToggleGroup = new ToggleGroup();
        cardOptionsToggleGroup.getToggles().addAll(amountFive, amountTen);

        Label gameNameLabel = new Label(bundle.getString("gl_gameNameLabel"));
        TextField gameNameTextField  = new TextField();

        Button createGameButton = new Button(bundle.getString("gl_create_game"));
       
        createGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ServerNewGameMessageHandler newGameHandler= new ServerNewGameMessageHandler();
                String actionCards=null;
                String maxPlayers= null;
                if(amountFive.isSelected()){
                    actionCards = "5";
                }else if(amountTen.isSelected()){
                    actionCards = "10";
                }
                if(twoPlayer.isSelected()){
                    maxPlayers="2";
                }else{ maxPlayers = "4";}
                String gameName = gameNameTextField.getText();
                String message = gameName+ "@"+actionCards + "@"+maxPlayers;
                newGameHandler.write(message, false);
            	isGameStartedProperty().setValue(true);
                isGameStartedProperty().setValue(false);
            }
        });

        
        rightOptions.getChildren().addAll(winCondition, shortGame, longGame, actionCards, amountFive, amountTen);

        gameOptions.getChildren().addAll(leftOptions, rightOptions);
        createGameBox.getChildren().addAll(gameOptions, gameNameLabel, gameNameTextField, createGameButton
        );
        return createGameBox;
    }



    private VBox createRight() {
        VBox right = new VBox(10);
        VBox topRight = createTopFive();
        VBox bottomRight = createChatArea();
        right.getChildren().addAll(topRight, bottomRight);
        return right;
    }

    private VBox createTopFive() {
        VBox topFive = new VBox(5);
        Label title = new Label(bundle.getString("gl_topfive_title"));
        topFive.getChildren().add(title);
        topLabels = new ArrayList<>();

        Label top1 = new Label("Peter Mayer");
        Label top2 = new Label("Peter Mayer");
        Label top3 = new Label("Peter Mayer");
        Label top4 = new Label("Peter Mayer");
        Label top5 = new Label("Peter Mayer");

        topFive.getChildren().addAll(top1, top2, top3, top4, top5);

        return topFive;
    }
    private VBox createChatArea() {
        VBox chatArea = new VBox(10);

        chat= new TextArea();
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
                chatHandler.write(chatText.getText(),false);
            }
        });

        chatArea.getChildren().addAll(chat, textInsert);
        return chatArea;

    }



    public void show(Stage stage) {
        Scene scene = new Scene(parent);
        stage.setTitle("Dominion");
        stage.setScene(scene);
        stage.show();
    }

    public SimpleBooleanProperty isGameStartedProperty() {
        return isGameStarted;
    }

}
