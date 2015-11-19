package main;

import java.util.HashMap;
import java.util.Map;


public class AccountService {
    private Map<String, UserProfile> users = new HashMap<>();
    private Map<String, UserProfile> sessions = new HashMap<>();
    private UserProfile anonim;
    private static Integer n_anonim = 1;
    @SuppressWarnings("all")
    private UserProfile getAnonim(String SessionId){
        String name = "anonim" + String.valueOf(n_anonim);
        n_anonim = ++n_anonim;
        String email = name + "@mail.ru";
        String pass = "12345";
        addUser(name, pass, email);
        addSessions(SessionId, getUser(name));
        return getCurrentUser(SessionId);
    }
    public boolean checkUser(String userName){
       if(users.containsKey(userName)) {
           return true;
       }
       else {
           return false;
       }
    }

    public String getSessinonId(UserProfile requestUser){

        for(Map.Entry<String, UserProfile> entry : sessions.entrySet()) {
            if(entry.getValue().equals(requestUser)){
                return entry.getKey();
            }
        }
        return "";
    }

    public boolean checkUserlogin(UserProfile user){
        return sessions.containsValue(user);
    }

    public int numberOfRegistered(){

        return users.size();
    }

    public int numberOfSessions(){

        return sessions.size();
    }

    public boolean addUser(String userName, String password, String email) {
        if (users.containsKey(userName))
            return false;
        UserProfile userProfile = new UserProfile(userName, password, email, users.size());
        users.put(userName, userProfile);
        return true;
    }

    public boolean checkSeassions(String sessionId){
        return sessions.containsKey(sessionId);
    }

    public boolean removeSeassions(String sessionId){
       if (!sessions.containsKey(sessionId))
           return false;
       sessions.remove(sessionId);
       return true;
    }

    public void addSessions(String sessionId, UserProfile userProfile) {
        sessions.put(sessionId, userProfile);
    }

    public UserProfile getCurrentUser(String sessionId){
        if(checkSeassions(sessionId)) {
            return sessions.get(sessionId);
        }
        return null;
    }

    public UserProfile getUser(String userName) {
        return users.get(userName);
    }

    public String userSession(String sessionId) {
        if (!sessions.containsKey(sessionId))
            return "";
        return sessions.get(sessionId).getLogin();
    }
    public UserProfile getSessions(String sessionId) {
        return sessions.get(sessionId);
    }

}
