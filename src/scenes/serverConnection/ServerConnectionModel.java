package scenes.serverConnection;

import util.LogHandling;


import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.logging.Level;



public class ServerConnectionModel {
    public boolean isConnected=false;
    private final String HOST = "127.0.0.1";
    private final int PORT = 9000;
    private Socket socket;

    public ServerConnectionModel() {
    }
    //@stefan tryconnect establishes the connection with the server via thread and listens if the service stops
    public void tryConnect() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        ExecutorCompletionService<Boolean> service =  new ExecutorCompletionService<>(executor);
        service.submit((Callable) () -> tryConnectToServer());
        executor.shutdown();

        while (!executor.isTerminated()) {
            tryTake(service);
        }
    }
    //tries to connect to the server via socket
    private Boolean tryConnectToServer() {
        try {
            socket = new Socket(HOST, 9000);

            return true;
        } catch (IOException e) {
            LogHandling.logOnFile(Level.SEVERE, ">> Failed to connect to Server");
            return false;
        }

    }
    // checks if the connection stays established
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
            LogHandling.logOnFile(Level.SEVERE, e.toString()); return false;
            //LogHandling.closeResources();
        } catch (ExecutionException e) {
            LogHandling.logOnFile(Level.SEVERE, e.toString()); return false;
        }
    }



    public String getHostAddress() {
        return HOST;
    }

    public Socket getSocket() {
        return socket;
    }
}
