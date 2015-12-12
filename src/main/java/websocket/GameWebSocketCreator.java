package websocket;

import main.AccountService;
import main.Context;
import main.UserProfile;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;


/**
 * Created by alla on 12.11.15.
 */
public class GameWebSocketCreator implements WebSocketCreator{

    @Override
    public Object createWebSocket(ServletUpgradeRequest request, ServletUpgradeResponse response) throws NullPointerException{
       // System.out.println("GameWebSocketCreator createWebSocket" + ses);
        String sessionId =request.getHttpServletRequest().getSession().getId();
        System.out.println("GameWebSocketCreator createWebSocket" + sessionId);
        try {
            Context instance = Context.getInstance();
            AccountService accountService = (AccountService)instance.get(AccountService.class);
            UserProfile currentUser =  accountService.getCurrentUser(sessionId);
            return new GameWebSocket(currentUser);
        }catch (NullPointerException e){
            e.printStackTrace();
            System.out.println("No user");
            throw e;

        }
     //   return new GameWebSocket(accountService.getCurrentUser(sessionId), gameWebSocketService, roomService);
    }
}
