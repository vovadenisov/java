package main;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by usr on 21.10.15.
 */
public class RoomService {
    private Map<Integer, Room> rooms = new HashMap<>();
//    private Boolean isChenged = false;
//    private Set<UserProfile> userInGame = new HashSet<>();

    public Integer putRoom(Room newRoom){
        if (rooms.containsValue(newRoom)){
            return -1;
        }
//        isChenged = true;
        rooms.put(newRoom.hashCode(), newRoom);
        return newRoom.hashCode();
    }

    public Room getRoom(Integer id){
        return rooms.get(id);
    }

    public Boolean userInRoom(UserProfile user){
        for (Map.Entry<Integer, Room> room : rooms.entrySet()) {
            if (room.getValue().getUsers().contains(user)) {
                return true;
            }
        }
        return false;
    }


//    public Set<UserProfile> getAllUserInGame(){
//        if (isChenged){
//            isChenged = false;
//            userInGame = allUserInGame();
//        }
//        return userInGame;
//    }

//    private Set<UserProfile> allUserInGame(){
//        Set<UserProfile> allUsers = new HashSet<>();
//        for(Map.Entry<Integer, Room> entry : rooms.entrySet()) {
//            allUsers.addAll(entry.getValue().getUsers());
//        }
//        return allUsers;
//    }
}