package frontend;

import main.AccountService;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class LogoutServlet extends HttpServlet {
    private AccountService accountService;
    public static final String LOGOUT_PAGE_URL = "/api/v1/auth/logout";
    public LogoutServlet(AccountService accountService) {
        this.accountService = accountService;
    }
    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        JSONObject json = new JSONObject();
        json.put("status", 200);
        String name =  accountService.userSession(request.getRequestedSessionId());
        if(name != ""){
            json.put("login", name);
            json.put("logout_status", accountService.removeSeassions(request.getSession().getId()));
            json.put("error_message", "correct");
        } else {
            json.put("login", "anonim");
            json.put("logout_status", false);
            json.put("error_message", "incorrect");
        }
        response.getWriter().println(json);
    }
}
