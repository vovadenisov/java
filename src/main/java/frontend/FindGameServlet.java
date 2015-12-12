package frontend;

import main.*;
import org.json.JSONObject;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by usr on 21.10.15.
 */
public class FindGameServlet extends HttpServlet {
    private AccountService  accountService;
    private UsersReadyToGameService usersReadyToGameService;
    private RoomService roomService;
    public static final String FIND_GAME_URL = "/api/v1/auth/find_rival";

    public FindGameServlet() {
        Context instance = Context.getInstance();

        this.accountService = (AccountService) instance.get(AccountService.class);
        this.roomService = (RoomService) instance.get(RoomService.class);
        this.usersReadyToGameService = (UsersReadyToGameService) instance.get(UsersReadyToGameService.class);
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        if(!accountService.checkSeassions(request.getSession().getId())) {
            response.setStatus(HttpServletResponse.SC_FOUND);
        }
        else {
            if (request.getParameter("is_game") == null) {
                Map<String, Object> pageVariables = new HashMap<>();
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(PageGenerator.getPage("find_list.html", pageVariables));
            }
            if (request.getParameter("is_game") != null){
                UserProfile currentUser = accountService.getCurrentUser(request.getSession().getId());
                Boolean find = false;
                JSONObject json = new JSONObject();
                for (int counter = 0; counter < 10; counter++) {
                    System.out.println("not hire login is");
                    System.out.println(currentUser.getLogin());
                    System.out.println("in room");
                    System.out.println(roomService.roomSize());
                    Room room = roomService.getRoom(roomService.getRoomWithUser(currentUser));
                    if (room != null) {
                        System.out.println("in room users:");
                        for (UserProfile _user : room.getUsers()) {
                            System.out.println(_user.getLogin());
                        }
                    }
                    else{
                        System.out.println("empty");
                        System.out.println("Users:");
                        Map<Integer, Room> rooms = roomService.getRooms();
                        for (Map.Entry<Integer, Room> _room : rooms.entrySet()) {
                            for (UserProfile __user : _room.getValue().getUsers()) {
                                System.out.println(__user.getLogin());
                                System.out.println(__user);
                                System.out.println(currentUser.getLogin());
                                System.out.println(currentUser);
                            }
                        }
                    }
                    if (roomService.userInRoom(currentUser)){
                        System.out.println("user in room login is");
                        System.out.println(currentUser.getLogin());
                        response.setStatus(HttpServletResponse.SC_OK);
                        json.put("game_status", 1);
                        response.getWriter().println(json);
                        find = true;
                        System.out.println("find = true, login is");
                        System.out.println(currentUser.getLogin());
                        System.out.println("in room");
                        System.out.println(roomService.roomSize());
                        System.out.println("Users:");
                        Map<Integer, Room> rooms = roomService.getRooms();
                        for (Map.Entry<Integer, Room> _room : rooms.entrySet()) {
                            for (UserProfile __user : _room.getValue().getUsers()) {
                                System.out.println(__user.getLogin());
                                System.out.println(__user);
                                System.out.println(currentUser.getLogin());
                                System.out.println(currentUser);
                            }
                        }
                        break;
                    }
                    Time.sleep(1000);
                }
                if (!find){
                    System.out.println("else hire user is");
                    System.out.println(currentUser.getLogin());
                    response.setStatus(HttpServletResponse.SC_OK);
                    json.put("game_status",0);
                    response.getWriter().println(json);
                }
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        if(!accountService.checkSeassions(request.getSession().getId())) {
            response.setHeader("location", SignInServlet.SIGNIN_PAGE_URL);
        }
        else {
            response.setStatus(HttpServletResponse.SC_OK);
            Map<String, Object> pageVariables = new HashMap<>();
            UserProfile userProfile = accountService.getCurrentUser(request.getSession().getId());
            usersReadyToGameService.addUserToReady(userProfile);
            JSONObject json = new JSONObject();
            json.put("status", "OK");
            response.setContentType("application/JSON");
            response.getWriter().println(json);
        }
    }
}
