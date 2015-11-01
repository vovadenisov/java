package frontend;

import main.AccountService;
import org.json.JSONObject;
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
        response.setStatus(HttpServletResponse.SC_OK);
        JSONObject json = new JSONObject();
        json.put("status", HttpServletResponse.SC_OK);
        json.put("registered", accountService.numberOfRegistered());
        json.put("online", accountService.numberOfSessions());
        String timeString = request.getParameter("shutdown");
        if (timeString != null) {
            System.out.println("");
            int timeMS = Integer.valueOf(timeString);
            System.out.print("Server will be down after: "+ timeMS + " ms");
            Time.sleep(timeMS);
            System.out.print("\nShutdown");
            System.exit(0);
        }
        response.getWriter().println(json);
    }

}
