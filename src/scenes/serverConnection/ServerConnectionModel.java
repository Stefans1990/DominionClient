package scenes.serverConnection;

import util.LogHandling;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServerConnectionModel {
    public boolean isConnected=false;
    private final String HOST = "127.0.0.1";
    private final int PORT = 9000;
    private Socket socket;

    public ServerConnectionModel() {
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
        try {
            socket = new Socket(HOST, 9000);
            
            return true;
        } catch (IOException e) {
            LogHandling.logOnFile(Level.SEVERE, ">> Failed to connect to Server");
            return false;
        }

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
            LogHandling.logOnFile(Level.SEVERE, e.toString()); return false;
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
