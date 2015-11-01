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


public class SignUpServlet extends HttpServlet {
    private AccountService accountService;
    public static final String SIGNUP_PAGE_URL = "/api/v1/auth/signup";
    public SignUpServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        JSONObject json = new JSONObject();
        if (accountService.addUser(name, new UserProfile(name, password, email))) {
            json.put("login", name == null ? "" : name);
            json.put("password", password == null ? "" : password);
            json.put("email", email == null ? "" : email);
            json.put("status", 200);
            json.put("login_status", true);
            json.put("error_massage", "New user created name: " + name);
            System.out.println("New user created name: " + name);

        } else {
            json.put("login", name == null ? "" : name);
            json.put("password", password == null ? "" : password);
            json.put("email", email == null ? "" : email);
            json.put("status", 200);
            json.put("login_status", false);
            json.put("error_massage", "User with name: " + name + " already exists");
            System.out.println("User with name: " + name + " already exists");
        }
        response.setStatus(HttpServletResponse.SC_OK);
        System.out.println(json.toJSONString());
        response.getWriter().println(json);
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("signUpStatus", "");
        response.setStatus(HttpServletResponse.SC_OK);
        JSONObject json = new JSONObject();
        if (accountService.checkSeassions(request.getSession().getId())) {
            UserProfile userProfile = accountService.getCurrentUser(request.getSession().getId());
            String name = userProfile.getLogin();
            String password = userProfile.getPassword();
            String email = userProfile.getEmail();
            json.put("login", name);
            json.put("password", password);
            json.put("email", email);
            json.put("status", 200);
            json.put("login_status", false);
            json.put("error_massage", "User with name: " + name + " already exists");
            System.out.println(json.toJSONString());
            response.getWriter().println(json);
        }
        else {
            response.getWriter().println(PageGenerator.getPage("signupstatus.html", pageVariables));
        }

    }
}
