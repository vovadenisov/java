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
        System.out.println("get");
        if (accountService.checkSeassions(request.getSession().getId())) {
            UserProfile userProfile = accountService.getCurrentUser(request.getSession().getId());
            String name = userProfile.getLogin();
            String password = userProfile.getPassword();
            pageVariables.put("name", name == null ? "" : name);
            pageVariables.put("password", password == null ? "" : password);
            pageVariables.put("login_status", "already logged");
            response.getWriter().println(PageGenerator.getPage("authresponse.txt", pageVariables));
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
        Map<String, Object> pageVariables = new HashMap<>();
        UserProfile profile = accountService.getUser(name);
        JSONObject json = new JSONObject();
        if (!accountService.checkSeassions(request.getSession().getId()) && !accountService.checkUserlogin(profile)) {
            if (profile != null && profile.getPassword().equals(password)) {
                json.put("login", name == null ? "" : name);
                json.put("password", password == null ? "" : password);
                json.put("status", 200);
                json.put("login_status", "true");
                pageVariables.put("login", name == null ? "" : name);
                pageVariables.put("password", password == null ? "" : password);
                pageVariables.put("login_status", "sucsess");
                request.setAttribute("user", profile);
                accountService.addSessions(request.getSession().getId(), profile);
                System.out.println("1");
            } else {
                pageVariables.put("login", name == null ? "" : name);
                pageVariables.put("password", password == null ? "" : password);
                pageVariables.put("login_status", "Wrong login/password");
                json.put("login", name == null ? "" : name);
                json.put("password", password == null ? "" : password);
                json.put("status", 200);
                json.put("login_status", "false");
                System.out.println("2");
            }
        }
        else{
            pageVariables.put("login", name == null ? "" : name);
            pageVariables.put("password", password == null ? "" : password);
            pageVariables.put("login_status", "already logged");
            json.put("login", name == null ? "" : name);
            json.put("password", password == null ? "" : password);
            json.put("status", 200);
            json.put("login_status", "false");
            System.out.println("3");
        }
        System.out.println("post");
        System.out.println(json.toJSONString());
        response.getWriter().println(json);
        //response.getWriter().println(PageGenerator.getPage("authresponse.txt", pageVariables));

    }
}
