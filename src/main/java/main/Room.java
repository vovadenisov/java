 package main;

import websocket.GameWebSocketService;

import java.sql.Blob;
import java.util.*;

 /**
  * Created by usr on 21.10.15.
  */
public class Room {
     private Team winer = null;
    private Map<Integer, Team> teams = new HashMap<>();
     private GameWebSocketService gameWebSocketService;
    private Set<Score> score = new HashSet<>();
    private long time = 0;
    private Boolean status = true;
     private Boolean game = false;
     private int level;
     private int step;
     private UserProfile first;
     private UserProfile second;
     private UserProfile current;
     public void stepGame(UserProfile user, String data){
         if(user.equals(first)) {
             gameWebSocketService.notifyStepGame(second, data);
         }else {
             gameWebSocketService.notifyStepGame(first, data);
         }
     }

     public void stepGameBin(UserProfile user, byte buf[]){
         System.out.println(user.getLogin() + " say " + buf);
         gameWebSocketService.notifyStepGameBinary(user, buf);
     }

     public Boolean getGame(){return game;}
     public void setGame(Boolean game){this.game = game;}

     public int getStep(){return step;}
     public void setStep(int step){this.step = step;}

     public int getLevel(){return this.level;}
     public void setLevel(int level){this.level = level;}

    public Room(Team team, GameWebSocketService gameWebSocketService ){
        Score score = new Score(team);
        teams.put(team.hashCode(), team);
        this.score.add(score);
        step = 0;
        level = 0;
        this.gameWebSocketService = gameWebSocketService;
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


    public void setStartTime (){
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


     public void startGameInRoom() {
         if(teams.size() == 2 && gameWebSocketService.userSocketsSize() == 2){
             starGame();
         }
     }

     private void starGame() {
         Team first = (Team)getTeams().toArray()[0];
         Team second = (Team)getTeams().toArray()[1];
         this.first = (UserProfile)first.getMembers().toArray()[0];
         this.second = (UserProfile)second.getMembers().toArray()[0];
         gameWebSocketService.notifyStartGame(this.first, this.second.getLogin());
         gameWebSocketService.notifyStartGame(this.second, this.first.getLogin());
         this.current = this.first;
         setGame(true);
        // userStep(1);
         usersSteps();
      //   System.out.println(getTime());
     }

     public void usersSteps(){
         setStartTime();
         gameWebSocketService.allCurrent();
     }

     public void userStep(int step){
         setStartTime();
         if( step == getStep()){
             return;
         }
         setStep(step);
         ++this.level;
         System.out.println("LEVEL " + level);
         if(current.equals(first)){
             current = second;
         }else {
             current = first;
         }
         currentUser();
     }

     public void currentUser(){
         gameWebSocketService.notifyCurrentUser(current);
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
     public Team getTeam(UserProfile user){
         for (Map.Entry<Integer, Team> team : teams.entrySet()) {
             if (team.getValue().checkUser(user)) {
                 return team.getValue();
             }
         }
         return null;
     }

     public long getTime(){
         return new Date().getTime() - time;
     }

   public void endGame(){
       gameWebSocketService.notifyGameOver();
     }

}
