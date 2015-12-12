package main;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by usr on 23.10.15.
 */
public class UsersReadyToGameService {
    private Set<UserProfile> users = new HashSet<>();

    public void popUserReady(UserProfile user){
        users.remove(user);
    }

    public void addUserToReady(UserProfile user){
        this.users.add(user);
    }

    public Set<UserProfile> getUserReady(){
        Set<UserProfile> allUser = new HashSet<>();
        allUser.addAll(users);
        return allUser;
    }

    public Boolean checkUser(String name){
        for (UserProfile user : users){
            if (Objects.equals(user.getLogin(), name)){
                return true;
            }
        }
        return false;
    }


}
