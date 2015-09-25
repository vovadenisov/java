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

public class SignInServlet extends HttpServlet {
    public static final String signinPageURL = "/api/v1/auth/signin";
    private AccountService accountService;
    public SignInServlet(AccountService accountService) {
        this.accountService = accountService;
    }
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();
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

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter("name");
        String password = request.getParameter("password");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();
        UserProfile profile = accountService.getUser(name);
        if (!accountService.checkSeassions(request.getSession().getId()) && !accountService.checkUserlogin(profile)) {
            if (profile != null && profile.getPassword().equals(password)) {
                pageVariables.put("name", name == null ? "" : name);
                pageVariables.put("password", password == null ? "" : password);
                pageVariables.put("login_status", "sucsess");
               // System.out.println("Login passed");
                request.setAttribute("user", profile);
                accountService.addSessions(request.getSession().getId(), profile);
            } else {
                pageVariables.put("name", name == null ? "" : name);
                pageVariables.put("password", password == null ? "" : password);
                pageVariables.put("login_status", "Wrong login/password");
             //   System.out.println("Wrong login/password");
            }
        }
       else{
            pageVariables.put("name", name == null ? "" : name);
            pageVariables.put("password", password == null ? "" : password);
            pageVariables.put("login_status", "already logged");
        }
        response.getWriter().println(PageGenerator.getPage("authresponse.txt", pageVariables));

    }
}
