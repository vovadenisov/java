package frontend;
import main.*;
import org.json.JSONArray;
import org.json.JSONObject;

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
    public GameServlet() {
        Context instance = Context.getInstance();
        this.accountService = (AccountService)instance.get(AccountService.class);
        this.roomService = (RoomService)instance.get(RoomService.class);
    }



    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        if (!accountService.checkSeassions(request.getSession().getId())){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
        else{
            UserProfile user = accountService.getCurrentUser(request.getSession().getId());
            if (request.getParameter("push") != null) {
                String roomId = request.getParameter("room_id");
                roomService.pushEvent("push", user, Integer.parseInt(roomId));
            }
            if (Objects.equals(request.getParameter("is_game_progress"), "true")) {
                if (!accountService.checkSeassions(request.getSession().getId())) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                } else {
                    String room_id = request.getParameter("room_id");
                    JSONArray winners_list = new JSONArray();
                  /*  if (!roomService.getRoom(Integer.parseInt(room_id)).getStatus()) {
                        for (UserProfile users : roomService.getRoom(Integer.parseInt(room_id)).getWiner().getMembers()) {
                            winners_list.add(users.getLogin());
                        }
                        json.put("is_game_progress", false);
                        json.put("winers", winnersList);
                    } else {
                        json.put("is_game_progress", true);
                    }*/
                    response.getWriter().println(json);
                }
            }
        }
    }
}