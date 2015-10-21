package frontend;

import main.AccountService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by usr on 21.10.15.
 */
public class FindGameServlet {
    private AccountService accountService;
    public static final String FIND_GAME_URL = "/api/v1/auth/find_Game";
    public FindGameServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {


    }
}
