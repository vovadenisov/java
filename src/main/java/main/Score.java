package main;

/**
 * Created by usr on 25.10.15.
 */
public class Score {
    private Integer score = 0;
    private Team team;

    public Score (Team team){this.team = team;}

    public void incrementScore(){
        score++;
    }

    public Integer getScore(){
        return score;
    }

    public Team getTeam(){
        return team;
    }


}
