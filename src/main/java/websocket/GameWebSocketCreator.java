package websocket;

import main.AccountService;
import main.RoomService;
import main.UserProfile;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import websocket.GameWebSocket;


/**
 * Created by alla on 12.11.15.
 */
public class GameWebSocketCreator implements WebSocketCreator{
    private AccountService accountService;
    private GameWebSocketService gameWebSocketService;
    private RoomService roomService;
    public GameWebSocketCreator(AccountService accountService, GameWebSocketService gameWebSocketService,
                                RoomService roomService){
        System.out.println("GameWebSocketCreator");
        this.accountService = accountService;
        this.gameWebSocketService = gameWebSocketService;
        this.roomService = roomService;
    }
    @Override
    public Object createWebSocket(ServletUpgradeRequest request, ServletUpgradeResponse response) throws NullPointerException{
       // System.out.println("GameWebSocketCreator createWebSocket" + ses);
        String sessionId =request.getHttpServletRequest().getSession().getId();
        System.out.println("GameWebSocketCreator createWebSocket" + sessionId);
        try {
           UserProfile currentUser =  accountService.getCurrentUser(sessionId);
            return new GameWebSocket(currentUser, gameWebSocketService, roomService);
        }catch (NullPointerException e){
            e.printStackTrace();
            System.out.println("No user");
            throw e;

        }
     //   return new GameWebSocket(accountService.getCurrentUser(sessionId), gameWebSocketService, roomService);
    }
}
