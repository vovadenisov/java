package frontend;

import main.AccountService;
import main.RoomService;
import main.UserProfile;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Created by usr on 26.10.15.
 */
public class GameInfoServlet extends HttpServlet {
    private AccountService accountService;
    private RoomService roomService;
    public static final String GAME_INFO_URL = "/api/v1/auth/game_status";

    public GameInfoServlet(AccountService accountService, RoomService roomService) {
        this.accountService = accountService;
        this.roomService = roomService;
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        UserProfile currentUser = accountService.getCurrentUser(request.getSession().getId());
        response.setContentType("application/json");
        if (currentUser != null && roomService.userInRoom(currentUser)){
            response.setStatus(HttpServletResponse.SC_OK);
            Integer roomId = roomService.getRoomWithUser(currentUser);
            Set<UserProfile> enemyUser = roomService.getRoom(roomId).getEnemyTeamUsers(currentUser);
            JSONArray userList = new JSONArray();
            for (UserProfile user : enemyUser) {
                JSONObject enemyUserJson = new JSONObject();
                enemyUserJson.put("id", user.getId());
                enemyUserJson.put("name", user.getLogin());
                userList.put(enemyUserJson.toString());
            }
            JSONObject currentUserJson = new JSONObject();
            currentUserJson.put("users",userList);
            currentUserJson.put("room_id", roomId);
            response.getWriter().println(currentUserJson);
        }
        else{
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
