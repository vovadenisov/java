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

        //тестирование
        System.out.println("in_get_get_user");
        for (UserProfile user : users_ready){
            System.out.println(user.getLogin());
        }

        //текущий юзер
        UserProfile current_user = accountService.getCurrentUser(request.getSession().getId());

        //убрать из юзеров, готовых к игре самого юзера
        users_ready.remove(current_user);

        //тестирование
        System.out.println("in_get_get_user2");
        for (UserProfile user : users_ready){
            System.out.println(user.getLogin());
        }

        //контейнер для юзеров, которых будем отдавать
        JSONArray user_login_list = new JSONArray();
        JSONObject user_object = new JSONObject();
        //наполнение контейнера
        for (UserProfile user : users_ready) {
            user_login_list.add(user.getLogin());
        }

        System.out.println(user_login_list.toString());

        JSONObject json = new JSONObject();
        //в json складываем юзеров, готовых к игре
        json.put("current_user", current_user.getLogin().toString());
        json.put("user_count", user_login_list.size());
        json.put("users", user_login_list);

        System.out.println("post");
        System.out.println(json.toJSONString());
        //отправляем json
        response.getWriter().println(json);

    }

}
