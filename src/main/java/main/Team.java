package main;


import java.util.HashSet;
import java.util.Set;

/**
 * Created by usr on 21.10.15.
 */
public class Team {
    private Integer teamSize = 1;
    private Set<UserProfile> members = new HashSet<>();

    public void setTeamSize(Integer size){
        if (size > 1){
            teamSize = size;
        }
    }

    public Integer getTeamSize(){
        return teamSize;
    }

    public Boolean addMembers(UserProfile member){
        if (members.size() < teamSize && !members.contains(member)) {
            members.add(member);
            return  true;
        }
        return false;
    }

    public Set<UserProfile> getMembers(){
        return members;
    }
}
