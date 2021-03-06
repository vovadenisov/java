package websocket;

import main.Context;
import main.Room;
import main.RoomService;
import main.UserProfile;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by alla on 12.11.15.
 */
@WebSocket
public class GameWebSocket {
    private Session session;
    private UserProfile user;
    private GameWebSocketService gameWebSocketService;
    private RoomService roomService;
    private Room room;
    private boolean status = false;

    public GameWebSocket( UserProfile user) {
      //  System.out.println("myName " + user.getLogin());
        this.user = user;
        Context instance = Context.getInstance();
        this.gameWebSocketService = (GameWebSocketService)instance.get(GameWebSocketService.class);
        this.roomService = (RoomService)instance.get(RoomService.class);
    }

    private void SetRoom(){
        Integer roomID = roomService.getRoomWithUser(user);
        if (roomID == -1){
            room = null;
        }else {
            room = roomService.getRoom(roomID);
        }
    }

    @OnWebSocketMessage
    public void onMessage(String data) throws IOException{
      //  System.out.println("Message " + user.getLogin() +" "+ data);
    /*    try {
            session.getRemote().sendString(data);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }*/
      //  if(status == true){
            room.stepGame(user, data);
      //  }
    }

    @OnWebSocketMessage
    public void onMessageBinary(byte buf[], int offset, int length) throws IOException{
     //   System.out.println("onMessageBinary " + user.getLogin());
        for (byte b : buf) {
            System.out.print(b);
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(buf);
        try {
            session.getRemote().sendBytes(byteBuffer);
        }catch (IOException e){
            e.printStackTrace();
            throw e;
        }

        if(status == true){
         //   room.stepGameBin(user, buf);
        }
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
    //    System.out.println("OnOpen " + user.getLogin());
        setSession(session);
        SetRoom();
        gameWebSocketService.addUser(this);
        room.startGameInRoom();
    }

    public Session getSession() {
        return session;
    }
    public void setSession(Session session) {
        this.session = session;
    }
    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {

    }
    public String getUserLogin(){
        return user.getLogin();
    }

    public void startGame(UserProfile user, String enemy) {
  //      System.out.println("startGame");
        try {
            JSONObject jsonStart = new JSONObject();
            jsonStart.put("status", "start");
            jsonStart.put("enemyName", enemy);
            session.getRemote().sendString(jsonStart.toJSONString());
     //       System.out.println(jsonStart.toJSONString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void gameOver() {
        try {
            JSONObject jsonStart = new JSONObject();
            jsonStart.put("status", "finish");
            //jsonStart.put("win", win);
            session.getRemote().sendString(jsonStart.toJSONString());
            System.out.println(jsonStart.toJSONString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }
    public void currentUser(UserProfile currentUser){
        try {
            JSONObject jsonStart = new JSONObject();
            jsonStart.put("status", "current");
            jsonStart.put("current", currentUser.getLogin());
            session.getRemote().sendString(jsonStart.toJSONString());
    //        System.out.println(jsonStart.toJSONString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }
    public void stepGame(UserProfile user, String data) {
        setStatus(true);
        // System.out.println("RESPONSE");
      // System.out.println(user.getLogin());
        try {
           /* JSONObject jsonStart = new JSONObject();
            jsonStart.put("status", "recipient");
            jsonStart.put("recipient", user.getLogin());
            session.getRemote().sendString(jsonStart.toJSONString());*/
            session.getRemote().sendString(data);

       //     System.out.println(jsonStart.toJSONString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }

    }


    public void stepGameBin(UserProfile user, byte buf[]) {
        setStatus(true);
        try {
            JSONObject jsonStart = new JSONObject();
            jsonStart.put("status", "step");
            jsonStart.put("current", user.getLogin());
            session.getRemote().sendString(jsonStart.toJSONString());
            System.out.println(jsonStart.toJSONString());
        } catch (Exception e) {
            System.out.print(e.toString());
        }

    }
    public void setStatus(boolean status){
        this.status = status;
    }
    public boolean getStatus(){
        return status;
    }
}
