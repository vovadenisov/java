package frontend;

import main.AccountService;
import templater.PageGenerator;

import main.Time;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminServlet extends HttpServlet {
   private AccountService accountService;
    public static final String ADMIN_PAGE_URL = "/api/v1/auth/admin";
    public AdminServlet(AccountService accountService) {
        this.accountService = accountService;
    }
    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        response.setStatus(HttpServletResponse.SC_OK);
        pageVariables.put("registr", accountService.number_of_registered());
        pageVariables.put("login", accountService.number_of_sessions());
        String timeString = request.getParameter("shutdown");
        if (timeString != null) {
            System.out.println("");
            int timeMS = Integer.valueOf(timeString);
            System.out.print("Server will be down after: "+ timeMS + " ms");
            Time.sleep(timeMS);
            System.out.print("\nShutdown");
            System.exit(0);
        }
        pageVariables.put("status", "run");
        response.getWriter().println(PageGenerator.getPage("adminresponse.txt", pageVariables));
    }

}
