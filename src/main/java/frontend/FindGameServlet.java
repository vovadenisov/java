package frontend;

import main.*;
import org.json.simple.JSONObject;
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
    private AccountService accountService;
    private UsersReadyToGameService usersReadyToGameService;
    private RoomService roomService;
    public static final String FIND_GAME_URL = "/api/v1/auth/find_rival";

    public FindGameServlet(AccountService accountService, UsersReadyToGameService usersReadyToGameService, RoomService roomService) {
        this.accountService = accountService;
        this.usersReadyToGameService = usersReadyToGameService;
        this.roomService = roomService;
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        //если юзера нет
        if(!accountService.checkSeassions(request.getSession().getId())) {
            response.setStatus(HttpServletResponse.SC_FOUND);
            response.setHeader("Location", SignInServlet.SIGNIN_PAGE_URL);
        }
        else {
            if (request.getParameter("is_game") == null) {
                Map<String, Object> pageVariables = new HashMap<>();
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(PageGenerator.getPage("find_list.html", pageVariables));
            }
            if (request.getParameter("is_game") != null){
                UserProfile current_user = accountService.getCurrentUser(request.getSession().getId());
                Boolean find = false;
                JSONObject json = new JSONObject();
                for (int counter = 0; counter < 10; counter++){
                    if (roomService.userInRoom(current_user)){
                        response.setStatus(HttpServletResponse.SC_OK);
                        json.put("game_status",1);
                        response.getWriter().println(json);
                        find = true;
                        break;
                    }
                    Time.sleep(1000);
                }
                if (!find){
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
        //если юзера нет
        if(!accountService.checkSeassions(request.getSession().getId())) {
            response.setHeader("location", SignInServlet.SIGNIN_PAGE_URL);
        }
        else {
            //если юзер авторизован, то добавить его в список игроков
            response.setStatus(HttpServletResponse.SC_OK);
            Map<String, Object> pageVariables = new HashMap<>();
            System.out.println("post");
            //получаем юзера
            UserProfile userProfile = accountService.getCurrentUser(request.getSession().getId());
            //добавляем юзера в игроков
            usersReadyToGameService.addUserToReady(userProfile);
            JSONObject json = new JSONObject();
            json.put("status", "OK");
            response.setContentType("application/JSON");
            response.getWriter().println(json);
        }
    }
}
