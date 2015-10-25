 package main;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by usr on 21.10.15.
 */
public class Room {
    private Map<Integer, Team> teams = new HashMap<>();
    private Set< Pair<Team, Integer> > score = new HashSet<>();

    public void IncrimentScore(Team team_score){
        for (Pair<Team, Integer> teamScore : score){

        }
    }

    public void getTeamUser(String user_name){

    }

    public boolean addTeam(Team newTeam){
        if (teams.size() < 2) {
            teams.put(newTeam.hashCode(), newTeam);
            return true;
        }
        return false;
    }



    public Set<Team> getTeams(){
        return (Set<Team>)teams.values();
    }

    public Set<UserProfile> getUsers(){
        Set<UserProfile> users = new HashSet<UserProfile>();
        for(Map.Entry<Integer, Team> team : teams.entrySet()){
            users.addAll(team.getValue().getMembers());
        }
        return users;
    }
}
