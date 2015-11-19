package websocket;
import main.AccountService;
import main.RoomService;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import templater.PageGenerator;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alla on 11.11.15.
 */
@WebServlet(name = "GameWebSocketServlet", urlPatterns = {"/api/v1/auth/gameplay"})
public class GameWebSocketServlet extends WebSocketServlet {
    public static final String GAME_WEB_SOCKET_URL = "/api/v1/auth/gameplay";
    private final static int IDLE_TIME = 60 * 1000;
    private AccountService accountService;
    private GameWebSocketService gameWebSocketService;
    private RoomService roomService;

    public GameWebSocketServlet(AccountService accountService, GameWebSocketService gameWebSocketService,
                                RoomService roomService){
        System.out.println("GameWebSocketServlet");
        this.accountService = accountService;
        this.gameWebSocketService = gameWebSocketService;
        this.roomService  = roomService;
    }
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        System.out.println("gamesoket " + request.getSession().getId());
        Map<String, Object> pageVariables = new HashMap<>();
        String name = accountService.getCurrentUser(request.getSession().getId()).getLogin();
        pageVariables.put("myName", name);
        response.getWriter().println(PageGenerator.getPage("game_2.html", pageVariables));
    }
    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(IDLE_TIME);
        factory.setCreator(new GameWebSocketCreator(accountService, gameWebSocketService, roomService));
    }

}
