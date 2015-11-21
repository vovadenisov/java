package main;

import db.dbServise.DBService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class AccountService {
//    private Map<String, UserProfile> users = new HashMap<>();
    private Map<String, UserProfile> sessions = new HashMap<>();
    private DBService dbService;

    public AccountService(DBService dbService){
        this.dbService = dbService;
    }

    @SuppressWarnings("all")
    public boolean checkUser(String userName){
        if (dbService.readByName(userName) != null){
            return true;
        }
        return false;
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
        return dbService.getCount();
    }

    public int numberOfSessions(){
        return sessions.size();
    }

    public boolean addUser(String userName, String password, String email) {
        try {
            dbService.saveUser(userName, password, email);
            return true;
        }
        catch (Exception e){
            return false;
        }
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
        return sessions.get(sessionId);
    }

    public UserProfile getUser(String userName) {
        return dbService.readByName(userName);
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
