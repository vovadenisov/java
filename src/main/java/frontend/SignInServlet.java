package frontend;

import main.*;
import org.json.JSONObject;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignInServlet extends HttpServlet {
        public static final String SIGNIN_PAGE_URL = "/api/v1/auth/signin";
        private AccountService accountService;
        public SignInServlet() {
            Context instance = Context.getInstance();
            this.accountService = (AccountService)instance.get(AccountService.class);
        }
        @Override
        public void doGet(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
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
        JSONObject json = new JSONObject();
        if (accountService.checkUser(name)){
            UserProfile profile = accountService.getUser(name);
            if (!accountService.checkUserlogin(profile)){
                if(profile.getPassword().equals(password)){
                    json.put("login", name == null ? "" : name);
                    json.put("password", password == null ? "" : password);
                    json.put("status", 200);
                    json.put("login_status", true);
                    json.put("error_massage", "");
                    request.setAttribute("user", profile);
                    accountService.addSessions(request.getSession().getId(), profile);
                }else {
                    json.put("login", name == null ? "" : name);
                    json.put("password", password == null ? "" : password);
                    json.put("status", 200);
                    json.put("login_status", false);
                    json.put("error_massage", "Wrong login/password");
                }

            }else {
                json.put("login", name == null ? "" : name);
                json.put("password", password == null ? "" : password);
                json.put("status", 200);
                json.put("login_status", false);
                json.put("error_massage", "already logged");
            }

        }else {
            json.put("login", name == null ? "" : name);
            json.put("password", password == null ? "" : password);
            json.put("status", 200);
            json.put("login_status", false);
            json.put("error_massage", "User with this name does not exist");
        }
        System.out.println(json.toString());
        response.getWriter().println(json);
    }
}