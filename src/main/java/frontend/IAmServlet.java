package frontend;

import main.AccountService;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by usr on 27.10.15.
 */
public class IAmServlet extends HttpServlet {
    private AccountService accountService;
    public static final String I_AM_URL = "/api/v1/auth/curruser";

    public IAmServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        JSONObject pageVariables = new JSONObject();
        if (accountService.checkSeassions(request.getSession().getId())){
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            pageVariables.put("current_user", accountService.getCurrentUser(request.getSession().getId()).getLogin());
            pageVariables.put("current_user_id", accountService.getCurrentUser(request.getSession().getId()).getId());
            response.getWriter().println(pageVariables);
        }
        else{
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}