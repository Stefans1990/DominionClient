package scenes.verification;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import util.ServiceLocator;

import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;

import Handlers.ServerLoginMessageHandler;
import Handlers.ServerRegisterMessageHandler;

public class VerificationView {
    private final VerificationModel model;
    private BorderPane parent;

    public VerificationView(VerificationModel model) {
        this.model = model;

    }

    public void createLayout() {

        ResourceBundle bundle = ServiceLocator.getResourceBundle();

        Label title = new Label(bundle.getString("vs_title"));

        VBox userPassBox = new VBox(15);
        userPassBox.setAlignment(Pos.CENTER);

        Label userNameLabel = new Label(bundle.getString("vs_username"));
        TextField userNameField = new TextField();
        userNameField.setMaxWidth(200);


        Label passwordLabel = new Label(bundle.getString("vs_password"));
        PasswordField passwordField = new PasswordField();
        passwordField.setMaxWidth(200);

        userPassBox.getChildren().addAll(userNameLabel, userNameField, passwordLabel, passwordField);

        Button loginButton = new Button(bundle.getString("vs_button_login"));

        Label comboBoxLabel = new Label(bundle.getString("vs_language"));

        ChoiceBox<String> languageChoiceBox = new ChoiceBox();

        languageChoiceBox.getItems().addAll("English", "Deutsch");

        languageChoiceBox.setOnAction(e -> {
            if (languageChoiceBox.getValue().equalsIgnoreCase("English")) {
                ServiceLocator.setLocale("en", "US");
            } else if (languageChoiceBox.getValue().equalsIgnoreCase("Deutsch")) {
                ServiceLocator.setLocale("de", "CH");
            }Locale.setDefault(ServiceLocator.getLocale());

        });

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ServerLoginMessageHandler loginHandler = new ServerLoginMessageHandler();
                String userName = userNameField.getText();
                String password = passwordField.getText();
                loginHandler.write(userName + "@" + password);


            }
        });

        Button signUpButton = new Button(bundle.getString("vs_button_signup"));

        signUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ServerRegisterMessageHandler registerHandler = new ServerRegisterMessageHandler();
                String userName = userNameField.getText();
                String password = passwordField.getText();
                registerHandler.write(userName + "@" + password);

            }
        });
        HBox buttonBox = new HBox(30);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(loginButton, signUpButton);
        HBox languageBox = new HBox(30);
        languageBox.setAlignment(Pos.CENTER);
        languageBox.getChildren().addAll(comboBoxLabel, languageChoiceBox);
        VBox content = new VBox(30);
        content.getChildren().addAll(title, languageBox, userPassBox, buttonBox);
        content.setMaxWidth(300);
        content.setAlignment(Pos.CENTER);
        parent = new BorderPane();
        BorderPane.setAlignment(content, Pos.CENTER);
        BorderPane.setMargin(content, new Insets(100));
        parent.setCenter(content);

    }

    public void show(Stage stage) {
        parent.setStyle("-fx-background-color: #333333");
        Scene scene = new Scene(parent);
        scene.getStylesheets().add("scenes/verification/verificationStyleSheet.css");
        stage.setTitle("Dominion");
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        stage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getMaxX() / 4);
        stage.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getMaxY() / 4);
        stage.setWidth(primaryScreenBounds.getWidth() / 2);
        stage.setHeight(primaryScreenBounds.getHeight() / 2);
        stage.setScene(scene);
        stage.show();
    }
}
