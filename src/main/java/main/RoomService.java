package main;

import websocket.GameWebSocketService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by usr on 21.10.15.
 */
public class RoomService {
    private Map<Integer, Room> rooms = new HashMap<>();
    private static final int STEP_TIME = 200;
    private static final int GAME_TIME = 20000;
    private static final int GAME_RAUND = 10000*4;
    private static final int USER_STEP_TIME = 100000;
    private int numberOfLevels = 1;
    private GameWebSocketService gameWebSocketService;
    private Team waiter;

    public Integer putRoom(Room newRoom){
        if (rooms.containsValue(newRoom)){
            return -1;
        }
        rooms.put(newRoom.hashCode(), newRoom);
        return newRoom.hashCode();
    }
    public void removeRoom(Integer roomId){
        rooms.remove(roomId);
    }

    public Boolean pushEvent(String event, UserProfile user, Integer id){
        if (!rooms.isEmpty()) {
            if (event == "push") {
                rooms.get(id).addEvent("push", user);
                return true;
            }
        }
        return false;
    }

    public Room getRoom(Integer id){
        return rooms.get(id);
    }

    public Boolean userInRoom(UserProfile user){
        for (Map.Entry<Integer, Room> room : rooms.entrySet()) {
            if (room.getValue().getUsers().contains(user) && room.getValue().getStatus()) {
                return true;
            }
        }
        return false;
    }



    public Integer getRoomWithUser(UserProfile user){
        for (Map.Entry<Integer, Room> room : rooms.entrySet()) {
            if (room.getValue().getUsers().contains(user) && room.getValue().getStatus()) {
                return room.getKey();
            }
        }
        return -1;
    }
    public Team getTeam(UserProfile user){
        if(userInRoom(user)){
            Room room = getRoom(getRoomWithUser(user));
            return room.getTeam(user);
        }
        return null;
    }

    public void Start(){
        System.out.println("RUN");
        while (true) {
            for (Map.Entry<Integer, Room> room : rooms.entrySet()) {
                if(room.getValue().getGame()) {
                    System.out.println("The room time :" + room.getValue().getTime());
                        if(room.getValue().getTime() > GAME_RAUND ){
                            room.getValue().userStep(room.getValue().getStep() + 1);
                            if(room.getValue().getLevel()%2 >= numberOfLevels){
                                room.getValue().endGame();
                                this.removeRoom(room.getKey());
                            }
                        }

                }
            }
            Time.sleep(100);
        }
    }


    public void simpleStart(){
        System.out.println("RUN");
        while (true) {
            for (Map.Entry<Integer, Room> room : rooms.entrySet()) {
                if(room.getValue().getGame()) {
                 //   System.out.println("The room time :" + room.getValue().getTime());
                    if(room.getValue().getTime() > GAME_RAUND ){
                            room.getValue().endGame();
                            this.removeRoom(room.getKey());
                    }

                }
            }
            Time.sleep(100);
        }
    }

}