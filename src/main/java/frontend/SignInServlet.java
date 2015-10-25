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
import org.json.simple.JSONObject;

public class SignInServlet extends HttpServlet {
    public static final String SIGNIN_PAGE_URL = "/api/v1/auth/signin";
    private AccountService accountService;
    public SignInServlet(AccountService accountService) {
        this.accountService = accountService;
    }
    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();
        if (accountService.checkSeassions(request.getSession().getId())) {
            JSONObject json = new JSONObject();
            UserProfile userProfile = accountService.getCurrentUser(request.getSession().getId());
            String name = userProfile.getLogin();
            String password = userProfile.getPassword();
            json.put("login", name == null ? "" : name);
            json.put("password", password == null ? "" : password);
            json.put("status", 200);
            json.put("login_status", false);
            json.put("error_massage", "Already logged");
            response.getWriter().println(json);
        }
        else {
            response.getWriter().println(PageGenerator.getPage("authstatus.html", pageVariables));
        }
    }

    @Override
    @SuppressWarnings("all")
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter("name");
        String password = request.getParameter("password");
        response.setStatus(HttpServletResponse.SC_OK);
        UserProfile profile = accountService.getUser(name);
        JSONObject json = new JSONObject();
        if (!accountService.checkUserlogin(profile)) {
            if (profile != null && profile.getPassword().equals(password)) {
                json.put("login", name == null ? "" : name);
                json.put("password", password == null ? "" : password);
                json.put("status", 200);
                json.put("login_status", true);
                json.put("error_massage", "");
                request.setAttribute("user", profile);
                accountService.addSessions(request.getSession().getId(), profile);
                System.out.println("sucsess");
            } else {
                json.put("login", name == null ? "" : name);
                json.put("password", password == null ? "" : password);
                json.put("status", 200);
                json.put("login_status", false);
                json.put("error_massage", "Wrong login/password");
                System.out.println("Wrong login/password");
            }
        }
        else{
            json.put("login", name == null ? "" : name);
            json.put("password", password == null ? "" : password);
            json.put("status", 200);
            json.put("login_status", false);
            json.put("error_massage", "already logged");
            System.out.println("already logged");
        }
        System.out.println("post");
        System.out.println(json.toJSONString());
        response.getWriter().println(json);
    }
}
