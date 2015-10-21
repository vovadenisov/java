package main;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by usr on 21.10.15.
 */
public class Room {
    private Set<Team> teams = new HashSet<>();
    private Team winer;

    public boolean addTeam(Team newTeam){
        if (teams.size() < 2) {
            teams.add(newTeam);
            return true;
        }
        return false;
    }

    public Set<Team> getTeams(){
        return teams;
    }

    public void setWinder(Team win){
        winer = win;
    }

    public Set<UserProfile> getUsers(){
        Set<UserProfile> users = new HashSet<UserProfile>();
        for(Team team : teams){
            users.addAll(team.getMembers());
        }
        return users;
    }

}
