import javafx.application.Application;
import javafx.stage.Stage;
import scenes.MainController;
import util.ServiceLocator;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DominionClient extends Application {

    private Logger logger;

    @Override
    public void init() throws Exception {
        //super.init();
        logger = Logger.getLogger(this.getClass().getCanonicalName());
        //TODO: load language from last session
        ServiceLocator.setLocale("en","US");
        Locale.setDefault(ServiceLocator.getLocale());
        //TODO: Logger
        logger.log(Level.INFO, ">> DominionClient Application started");
    }

    @Override
    public void start(Stage stage) {
        MainController mainController = new MainController();
        mainController.setStage(stage);
        mainController.runApplication();
    }


    @Override
    public void stop() throws Exception {
        super.stop();
    }


}
