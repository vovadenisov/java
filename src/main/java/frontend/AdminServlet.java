package frontend;

import main.AccountService;
import main.UserProfile;
import templater.PageGenerator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminServlet extends HttpServlet {
   private AccountService accountService;

    public AdminServlet(AccountService accountService) {
        this.accountService = accountService;
    }
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        response.setStatus(HttpServletResponse.SC_OK);
        pageVariables.put("registr", accountService.number_of_registered());
        pageVariables.put("login", accountService.number_of_sessions());
        response.getWriter().println(PageGenerator.getPage("adminresponse.txt", pageVariables));
    }

}
