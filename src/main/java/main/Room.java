 package main;

import java.util.*;

 /**
  * Created by usr on 21.10.15.
  */
public class Room {
    private Map<Integer, Team> teams = new HashMap<>();
    private Set<Score> score = new HashSet<>();
    private long time = 0;
    private Boolean status = true;
    private Team winer = null;

    public Room(Team team){
        Score teamScore = new Score(team);
        teams.put(team.hashCode(), team);
        this.score.add(teamScore);
    }

    public boolean getStatus(){
        return status;
    }

    public Team getWiner(){
        return winer;
    }

    public boolean addEvent(String event, UserProfile user){
        if (Objects.equals("push", event)){
            if (status){
                status = false;
                for( Team team : getTeams()){
                    if (team.getMembers().contains(user)){
                        winer = team;
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public void setStartTime (long time){
        Date date = new Date();
        this.time = date.getTime();
    }

    public boolean isFinish(){
        Date data = new Date();
        return this.time + 10000 < data.getTime();
    }

    public void incrimentScore(Team team){
        for (Score teamScore : score) {
            if (teamScore.isTeam(team)) {
                teamScore.incrementScore();
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
        for(UserProfile user : newTeam.getMembers()){
            if(this.getUsers().contains(user)){
                return false;
            }
        }
        if (teams.size() < 2) {
            teams.put(newTeam.hashCode(), newTeam);
            return true;
        }
        return false;
    }



    public Set<Team> getTeams(){
        Set<Team> valueSet = new HashSet<Team>();
        for(Integer team : teams.keySet()){
            valueSet.add(teams.get(team));
        }
        return valueSet;
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
