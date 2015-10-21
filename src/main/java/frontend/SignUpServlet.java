package frontend;

import main.AccountService;
import main.UserProfile;
import org.json.simple.JSONObject;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


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
        Map<String, Object> pageVariables = new HashMap<>();
        JSONObject json = new JSONObject();
        if (accountService.addUser(name, new UserProfile(name, password, ""))) {
            pageVariables.put("name", name == null ? "" : name);
            pageVariables.put("password", password == null ? "" : password);
            pageVariables.put("email", email == null ? "" : email);
            pageVariables.put("signup_status", "New user created name: " + name);
            json.put("login", name == null ? "" : name);
            json.put("password", password == null ? "" : password);
            json.put("status", 200);
            json.put("login_status", "true");
            System.out.println("New user created name: " + name);

        } else {
            pageVariables.put("name", name == null ? "" : name);
            pageVariables.put("password", password == null ? "" : password);
            pageVariables.put("email", email == null ? "" : email);
            pageVariables.put("signup_status", "User with name: " + name + " already exists");
            json.put("login", name == null ? "" : name);
            json.put("password", password == null ? "" : password);
            json.put("status", 200);
            json.put("login_status", "false");
            System.out.println("User with name: " + name + " already exists");
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        System.out.println(json.toJSONString());
        response.getWriter().println(json);
      //  response.getWriter().println(PageGenerator.getPage("signupresponse.txt", pageVariables));

    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("signUpStatus", "");
        response.setStatus(HttpServletResponse.SC_OK);
        if (accountService.checkSeassions(request.getSession().getId())) {
            System.out.println("tut");
            UserProfile userProfile = accountService.getCurrentUser(request.getSession().getId());
            String name = userProfile.getLogin();
            String password = userProfile.getPassword();
            pageVariables.put("name", name == null ? "" : name);
            pageVariables.put("password", password == null ? "" : password);
            pageVariables.put("login_status", "already logged");
            response.getWriter().println(PageGenerator.getPage("authresponse.txt", pageVariables));
        }
        else {
            response.getWriter().println(PageGenerator.getPage("signupstatus.html", pageVariables));
        }

    }
}
