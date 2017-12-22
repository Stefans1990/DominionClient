import javafx.application.Application;
import javafx.stage.Stage;
import scenes.MainController;
import util.LogHandling;
import util.ServiceLocator;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import Handlers.MessageHandlerFactory;

public class DominionClient extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    /*
    * this class is responsible for the
    * */
    @Override
    public void init() throws Exception {
        ServiceLocator.setLocale("de", "CH");
        Locale.setDefault(ServiceLocator.getLocale());
        LogHandling.logOnFile(Level.INFO, ">> DominionClient Application started");
    }

    @Override
    public void start(Stage stage) throws Exception {
        MainController mainController = new MainController();
        mainController.setStage(stage);
        mainController.runApplication();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }


}
