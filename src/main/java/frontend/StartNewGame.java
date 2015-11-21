package frontend;

import main.*;
import org.json.JSONObject;

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
    public static final String INVITE_URL = "/api/v1/auth/invite";

    public StartNewGame() {
        Context instance = Context.getInstance();
        this.accountService = (AccountService)instance.get(AccountService.class);
        this.roomService = (RoomService)instance.get(RoomService.class);
        this.usersReadyToGameService = (UsersReadyToGameService)instance.get(UsersReadyToGameService.class);
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("user");
        if (userName == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        else{
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            JSONObject json = new JSONObject();
            if (usersReadyToGameService.checkUser(userName)){
                json.put("user_status", "request_user_found");
                UserProfile currentUser = accountService.getCurrentUser(request.getSession().getId());
                UserProfile inviteUser = accountService.getUser(userName);
                Team currentUserTeam = new Team();
                Team inviteUserTeam = new Team();
                if (currentUserTeam.addMembers(currentUser) && inviteUserTeam.addMembers(inviteUser)) {
                    Room gameRoom = new Room(currentUserTeam);
                    if (gameRoom.addTeam(inviteUserTeam)) {
                        roomService.putRoom(gameRoom);
                        json.put("game_status", true);
                        usersReadyToGameService.popUserReady(inviteUser);
                        usersReadyToGameService.popUserReady(currentUser);
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
