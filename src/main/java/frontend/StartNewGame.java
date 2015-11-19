package frontend;

import main.*;
import org.json.simple.JSONObject;
import websocket.GameWebSocketService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by usr on 24.10.15.
 */
public class StartNewGame extends HttpServlet {
    private UsersReadyToGameService usersReadyToGameService;
    private AccountService accountService;
    private RoomService roomService;
    private GameWebSocketService gameWebSocketService;
    public static final String INVITE_URL = "/api/v1/auth/invite";

    public StartNewGame(UsersReadyToGameService usersReadyToGameService, AccountService accountService, RoomService roomService,
                         GameWebSocketService gameWebSocketService) {
        this.roomService = roomService;
        this.usersReadyToGameService = usersReadyToGameService;
        this.accountService = accountService;
        this.gameWebSocketService = gameWebSocketService;
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        System.out.println("StartNewGame");
        String user_name = request.getParameter("user");
        if (user_name == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        else{
            System.out.println(user_name + " " + request.getSession().getId());
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            JSONObject json = new JSONObject();
            if (usersReadyToGameService.checkUser(user_name)){
                json.put("user_status", "request_user_found");
                UserProfile current_user = accountService.getCurrentUser(request.getSession().getId());
                UserProfile invite_user = accountService.getUser(user_name);
                Team current_user_team = new Team();
                Team invite_user_team = new Team();
                if (current_user_team.addMembers(current_user) && invite_user_team.addMembers(invite_user)) {
                    Room game_room = new Room(current_user_team, gameWebSocketService);
                    if (game_room.addTeam(invite_user_team)) {
                        roomService.putRoom(game_room);
                        json.put("game_status", true);
                        usersReadyToGameService.popUserReady(invite_user);
                        usersReadyToGameService.popUserReady(current_user);
                        response.getWriter().println(json);
                    }
                    else{
                        json.put("game_status", "can't_add_team_in_room");
                        response.getWriter().println(json);
                    }
                }
                else {
                    json.put("game_status", "can't_add_user_in_team");
                    response.getWriter().println(json);
                }
            }
            else{
                json.put("game_status",false);
                response.getWriter().println(json);
            }
        }

    }

}
