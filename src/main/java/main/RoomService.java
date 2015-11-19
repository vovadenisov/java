package main;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by usr on 21.10.15.
 */
public class RoomService {
    private Map<Integer, Room> rooms = new HashMap<>();

    public Integer putRoom(Room newRoom){
        if (rooms.containsValue(newRoom)){
            return -1;
        }
        rooms.put(newRoom.hashCode(), newRoom);
        return newRoom.hashCode();
    }

    public Boolean pushEvent(String event, UserProfile user, Integer id){
        if (!rooms.isEmpty()) {
            if (Objects.equals(event, "push")) {
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

}