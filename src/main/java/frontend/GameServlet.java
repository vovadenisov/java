package frontend;
import main.AccountService;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by usr on 21.10.15.
 */
public class GameServlet extends HttpServlet {
    private AccountService accountService;
    public static final String GAME_PAGE_URL = "/api/v1/auth/Game_logic";
    public GameServlet(AccountService accountService) {
        this.accountService = accountService;
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