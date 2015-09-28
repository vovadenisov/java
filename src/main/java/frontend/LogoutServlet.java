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

public class LogoutServlet extends HttpServlet {
    private AccountService accountService;
    public static final String logoutPageURL = "/api/v1/auth/logout";
    public LogoutServlet(AccountService accountService) {
        this.accountService = accountService;
    }
    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("name", accountService.UserSession(request.getRequestedSessionId()));
        boolean status = accountService.removeSeassions(request.getSession().getId());
        if(status)
            pageVariables.put("logout_status", "true");
        else
            pageVariables.put("logout_status", "false");
        response.getWriter().println(PageGenerator.getPage("logoutresponse.txt", pageVariables));
    }

}
