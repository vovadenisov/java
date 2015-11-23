package main;

import frontend.*;
import websocket.GameWebSocketService;
import websocket.GameWebSocketServlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import parser.ConfigParser;
import parser.XMLReader;
import java.io.File;
import java.io.IOException;


/**
 * Created by alla edited by nastya on 16.09.15.
 *
 */
public class Main {
    public static void main(String[] args) throws NumberFormatException, InterruptedException, IOException {
        ConfigParser configParser = new ConfigParser();
        String portString = configParser.getPort();
        int port = Integer.valueOf(portString);
        System.out.append("Starting at port: ").append(portString).append('\n');
        XMLReader xmlReader = new XMLReader();
        AccountService accountService = new AccountService();
        try {
            UserProfile User = (UserProfile) xmlReader.readXML("data" + File.separator + "user.xml");
            accountService.addUser(User.getLogin(), User.getPassword(), User.getEmail());
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("User Error");
        }
        try {
            UserProfile Admin = (UserProfile) xmlReader.readXML("data" + File.separator + "some.xml");
            accountService.addUser(Admin.getLogin(), Admin.getPassword(), Admin.getEmail());
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Admin Error");
        }
        GameWebSocketService gameWebSocketService = new GameWebSocketService();
        RoomService roomService = new RoomService();
        UsersReadyToGameService usersReadyToGameService = new UsersReadyToGameService();
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new GameWebSocketServlet(accountService, gameWebSocketService, roomService)), GameWebSocketServlet.GAME_WEB_SOCKET_URL);
        context.addServlet(new ServletHolder(new SignInServlet(accountService)), SignInServlet.SIGNIN_PAGE_URL );
        context.addServlet(new ServletHolder(new SignUpServlet(accountService)), SignUpServlet.SIGNUP_PAGE_URL );
        context.addServlet(new ServletHolder(new LogoutServlet(accountService)), LogoutServlet.LOGOUT_PAGE_URL);
        context.addServlet(new ServletHolder(new GameServlet(accountService, roomService)), GameServlet.GAME_PAGE_URL);
        context.addServlet(new ServletHolder(new FindGameServlet(accountService, usersReadyToGameService, roomService)), FindGameServlet.FIND_GAME_URL);
        context.addServlet(new ServletHolder(new AdminServlet(accountService)), AdminServlet.ADMIN_PAGE_URL);
        context.addServlet(new ServletHolder(new GetReadyUserServlet(usersReadyToGameService, accountService)), GetReadyUserServlet.GET_USER_URL);
        context.addServlet(new ServletHolder(new StartNewGame(usersReadyToGameService, accountService, roomService, gameWebSocketService)), StartNewGame.INVITE_URL);
        context.addServlet(new ServletHolder(new GameInfoServlet(accountService, usersReadyToGameService, roomService)), GameInfoServlet.GAME_INFO_URL);
        context.addServlet(new ServletHolder(new IAmServlet(accountService)), IAmServlet.I_AM_URL );
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        Server server = new Server(port);
        server.setHandler(handlers);

        try {
            server.start();
         //   roomService.Start();
            roomService.simpleStart();
        } catch(Exception e) {
            throw new RuntimeException("error", e);
        }
        server.join();
    }

}
