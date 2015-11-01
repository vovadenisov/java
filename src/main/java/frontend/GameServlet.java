package frontend;
import main.AccountService;
import main.UserProfile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
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
public class GameServlet extends HttpServlet {
    private AccountService accountService;
    public static final String GAME_PAGE_URL = "/api/v1/auth/game";
    public GameServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        response.setStatus(HttpServletResponse.SC_OK);
        JSONObject json = new JSONObject();
        if(request.getParameter("room") != null) {
            String room_id = request.getParameter("room");
            if (request.getParameter("start") != null) {
                //запуск игры. установить начальное время в комнате по id
                UserProfile user = accountService.getCurrentUser(request.getSession().getId());

            }
        }
        else{
            response.getWriter().println(PageGenerator.getPage("Game.html", pageVariables));
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