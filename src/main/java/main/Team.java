package main;


import java.util.HashSet;
import java.util.Set;

/**
 * Created by usr on 21.10.15.
 */
public class Team {
    private Integer teamSize;
    private Set<UserProfile> members = new HashSet<>();

    public void setTeamSize(Integer size){
        teamSize = size;
    }

    public Boolean addMembers(UserProfile member){
        if (members.size() < teamSize) {
            members.add(member);
            return  true;
        }
        return false;
    }

    public Set<UserProfile> getMembers(){
        return members;
    }
}
