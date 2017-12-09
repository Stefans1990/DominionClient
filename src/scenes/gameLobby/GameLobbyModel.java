package scenes.gameLobby;


import java.util.ArrayList;

public class GameLobbyModel {

    private ArrayList<String> topFives;


    public ArrayList<String> getTopFive() {
        if (topFives == null){
            topFives = new ArrayList<>(5);
            for(String s: topFives){
                s = "-";
            }
        }
        return topFives;
    }

/*    public ArrayList<String> setTopFive(){

    }*/
}
