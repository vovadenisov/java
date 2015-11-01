package frontend;

import main.AccountService;
import main.RoomService;
import main.UserProfile;
import main.UsersReadyToGameService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
    private UsersReadyToGameService usersReadyToGameService;
    private RoomService roomService;
    public static final String GAME_INFO_URL = "/api/v1/auth/game_status";

    public GameInfoServlet(AccountService accountService, UsersReadyToGameService usersReadyToGameService, RoomService roomService) {
        this.accountService = accountService;
        this.usersReadyToGameService = usersReadyToGameService;
        this.roomService = roomService;
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        UserProfile current_user = accountService.getCurrentUser(request.getSession().getId());
        response.setContentType("application/json");
        if (roomService.userInRoom(current_user)){
            response.setStatus(HttpServletResponse.SC_OK);
            Integer room_id = roomService.getRoomWithUser(current_user);
            Set<UserProfile> enemy_user = roomService.getRoom(room_id).getEnemyTeamUsers(current_user);
            JSONObject json = new JSONObject();
            JSONArray user_list = new JSONArray();
            for (UserProfile user : enemy_user) {
                json.put("id", user.getId());
                json.put("name", user.getLogin());
                user_list.add(json);
                json.clear();
            }
            json.put("users",user_list);
            json.put("room_id", room_id);
        }
        else{
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
        //пустой запрос возвращаем: 200, если игра есть, имя противника, id комнаты
        //если комнаты нет то 403

    }
}
