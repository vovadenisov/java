package frontend;

import main.AccountService;
import main.UserProfile;
import main.UsersReadyToGameService;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Created by usr on 23.10.15.
 */
public class GetReadyUserServlet extends HttpServlet {

    private UsersReadyToGameService usersReadyToGameService;
    private AccountService accountService;
    public static final String GET_USER_URL = "/api/v1/auth/get_users";
    public GetReadyUserServlet( UsersReadyToGameService usersReadyToGameService, AccountService accountService) {
        this.accountService = accountService;
        this.usersReadyToGameService = usersReadyToGameService;
    }
    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        Set<UserProfile> userReady = usersReadyToGameService.getUserReady();

        if (accountService.checkSeassions(request.getSession().getId())) {
            UserProfile currentUser = accountService.getCurrentUser(request.getSession().getId());
            userReady.remove(currentUser);
            JSONArray userLoginList = new JSONArray();
            for (UserProfile user : userReady) {
                JSONObject userObject = new JSONObject();
                userObject.put("id", user.getId());
                userObject.put("name", user.getLogin());
                userLoginList.put(userObject);
            }
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.getWriter().println(userLoginList);
        }
        else{
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

}
