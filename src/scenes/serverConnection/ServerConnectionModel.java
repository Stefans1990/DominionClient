package scenes.serverConnection;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServerConnectionModel {
    public boolean isConnected;
    private final String HOST = "127.0.0.1";
    private final int PORT = 9000;
    private final Logger logger;
    private Socket socket;

    public ServerConnectionModel() {
        logger = Logger.getLogger("ServerConnectionModel");
    }

    public void tryConnect() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        ExecutorCompletionService<Boolean> service =  new ExecutorCompletionService<>(executor);
        service.submit((Callable) () -> tryConnectToServer());
        executor.shutdown();

        while (!executor.isTerminated()) {
            tryTake(service);
        }
    }

    private Boolean tryConnectToServer() {
        /*try {
            //socket = new Socket(HOST, 9000);

            //Todo: Connect to server and wait for answer


            return true;
        } catch (IOException e) {
            logger.log(Level.SEVERE, ">> Failed to connect to Server");
        }*/
        return true;
    }

    private void tryTake(ExecutorCompletionService<Boolean> service) {
        try {
            Future<Boolean> result = service.take();
            isConnected = tryGetConnectionResult(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Boolean tryGetConnectionResult(Future<Boolean> result) {
        try {
            return result.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            //TODO: log this
            System.out.println("");
            e.printStackTrace();
        } return false;
    }


    public String getHostAddress() {
        return HOST;
    }

    public Socket getSocket() {
        return socket;
    }
}
