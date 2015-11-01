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

        System.out.println(users.hashCode());
        System.out.println("addUserReady");
        for (UserProfile new_user : users){
            System.out.println(new_user.getLogin());
        }
        System.out.println("addUser");
        this.users.add(user);
        System.out.println("afterUserAdd");
        for (UserProfile new_user : users){
            System.out.println(new_user.getLogin());
        }
    }

    public Set<UserProfile> getUserReady(){
        Set<UserProfile> all_user = new HashSet<>();
        all_user.addAll(users);
        return all_user;
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
