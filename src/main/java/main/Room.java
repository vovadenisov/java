 package main;

import java.util.*;

 /**
  * Created by usr on 21.10.15.
  */
public class Room {
    private Map<Integer, Team> teams = new HashMap<>();
    private Set<Score> score = new HashSet<>();
    private long time = 0;

    public void setStartTime (long time){
        Date date = new Date();
        this.time = date.getTime();
    }

    public boolean isFinish(){
        Date data = new Date();
        return this.time + 10000 < data.getTime();
    }

    public void incrimentScore(Team team){
        for (Score team_score : score) {
            if (team_score.isTeam(team)) {
                team_score.incrementScore();
            }
        }
    }

    public Integer getTeamUser(UserProfile user){
        for (Map.Entry<Integer, Team> teamEntry : teams.entrySet()){
            if (teamEntry.getValue().getMembers().contains(user)){
                return teamEntry.getKey();
            }
        }
        return -1;
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

    public Set<UserProfile> getMyTeamUsers(UserProfile user){
        for (Map.Entry<Integer, Team> team : teams.entrySet()){
            if(team.getValue().getMembers().contains(user)){
               return  team.getValue().getMembers();
            }
        }
        return null;
    }

    public Set<UserProfile> getEnemyTeamUsers(UserProfile user){
        for (Map.Entry<Integer, Team> team : teams.entrySet()){
            if(!team.getValue().getMembers().contains(user)){
                return  team.getValue().getMembers();
            }
        }
        return null;
    }
}
