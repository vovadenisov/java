package websocket;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;

/**
 * Created by alla on 11.11.15.
 */
@WebServlet(name = "GameWebSocketServlet", urlPatterns = {"/api/v1/auth/gameplay"})
public class GameWebSocketServlet extends WebSocketServlet {
    public static final String GAME_WEB_SOCKET_URL = "/api/v1/auth/gameplay";
    private final static int IDLE_TIME = 60 * 1000;


    @Override
    public void configure(WebSocketServletFactory factory) {
        System.out.println("GameWebSocketServlet configure");
        factory.getPolicy().setIdleTimeout(IDLE_TIME);
        factory.setCreator(new GameWebSocketCreator());
    }

}
