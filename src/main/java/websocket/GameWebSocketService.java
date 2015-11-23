package websocket;
import main.UserProfile;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by alla on 15.11.15.
 */
public class GameWebSocketService {
    private Map<String, GameWebSocket> userSockets = new HashMap<>();

    public int userSocketsSize(){ return userSockets.size();}

    public void addUser(GameWebSocket user) {
        if(userSockets.containsValue(user)){
            return;
        }
        userSockets.put(user.getUserLogin(), user);
    }
    public void notifyStartGame(UserProfile user, String ememy) {
        GameWebSocket gameWebSocket = userSockets.get(user.getLogin());
        gameWebSocket.startGame(user, ememy);

    }
    public void notifyCurrentUser(UserProfile user){
        for(Map.Entry<String, GameWebSocket> socket : userSockets.entrySet()){
            socket.getValue().currentUser(user);
            if(socket.getKey() == user.getLogin()){
                System.out.print("current " + user.getLogin());
                socket.getValue().setStatus(true);
            }else {
                socket.getValue().setStatus(false);
            }
        }
    }

    public void allCurrent(){
        for(Map.Entry<String, GameWebSocket> socket : userSockets.entrySet()){
                socket.getValue().setStatus(true);
        }
    }

    public void notifyStepGame(UserProfile user, String data){
        GameWebSocket gameWebSocket = userSockets.get(user.getLogin());
        gameWebSocket.stepGame(user, data);
    }

    public void notifyStepGameBinary(UserProfile user, byte buf[]){
        GameWebSocket gameWebSocket = userSockets.get(user.getLogin());
        gameWebSocket.stepGameBin(user, buf);
    }
    public boolean getStatus(UserProfile user){
        GameWebSocket gameWebSocket = userSockets.get(user.getLogin());
        return gameWebSocket.getStatus();
    }
    public void notifyGameOver() {
        for(Map.Entry<String, GameWebSocket> socket : userSockets.entrySet()){
            socket.getValue().gameOver();
        }
    }
}
