package frontend;
import main.AccountService;
import main.Room;
import main.RoomService;
import main.UserProfile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by usr on 21.10.15.
 */
public class GameServlet extends HttpServlet {
    private AccountService accountService;
    private RoomService roomService;
    public static final String GAME_PAGE_URL = "/api/v1/auth/game";
    public GameServlet(AccountService accountService, RoomService roomService) {
        this.accountService = accountService;
        this.roomService = roomService;
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        UserProfile user = accountService.getCurrentUser(request.getSession().getId());
        if (Objects.equals(request.getParameter("push"), "true")) {
            Integer id = roomService.getRoomWithUser(user);
            Room room = roomService.getRoom(id);
            room.addEvent("push", user);

        }
        if (Objects.equals(request.getParameter("is_game_progress"), "true")){
            Integer room_id = roomService.getRoomWithUser(user);
            JSONArray winners_list = new JSONArray();
            if (!roomService.getRoom(room_id).getStatus()){
                for (UserProfile users : roomService.getRoom(room_id).getWiner().getMembers()){
                    winners_list.add(users.getLogin());
                }
                json.put("is_game_progress", "false");
                json.put("winers", winners_list);
            }
            else{
                json.put("is_game_progress", "true");
            }
            response.getWriter().println(json);
        }
    }

    @Override
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        JSONArray array = (JSONArray)JSONValue.parse(request.getParameter("notes"));
        System.out.println(array);

    }
}