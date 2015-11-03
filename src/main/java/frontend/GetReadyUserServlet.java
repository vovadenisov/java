package frontend;

import main.AccountService;
import main.UserProfile;
import main.UsersReadyToGameService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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

        //получаем юзеров, готовых к игре
        Set<UserProfile> users_ready = usersReadyToGameService.getUserReady();

        //текущий юзер
        UserProfile current_user = accountService.getCurrentUser(request.getSession().getId());

        //убрать из юзеров, готовых к игре самого юзера
        users_ready.remove(current_user);


        //контейнер для юзеров, которых будем отдавать
        JSONArray user_login_list = new JSONArray();
        //наполнение контейнера
        for (UserProfile user : users_ready) {
            JSONObject user_object = new JSONObject();
            user_object.put("id",user.getId());
            user_object.put("name",user.getLogin());
            user_login_list.add(user_object.clone());
            user_object.clear();
        }
        response.setContentType("application/json");
        response.getWriter().println(user_login_list);

    }

}
