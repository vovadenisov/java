package main;

import db.dbServise.DBService;
import frontend.*;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.xml.sax.SAXException;
import parser.ConfigParser;
import parser.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by alla edited by nastya on 16.09.15.
 *
 */

public class Main {
    public static void main(String[] args) throws NumberFormatException, InterruptedException, IOException, ParserConfigurationException, SAXException, SQLException {
        try {
            ConfigParser configParser = new ConfigParser();
            String portString = configParser.getPort();

            int port = Integer.valueOf(portString);
            System.out.append("Starting at port: ").append(portString).append('\n');
            XMLReader xmlReader = new XMLReader();
            DBService dbService;
            dbService = new DBService(configParser.getDBUser(), configParser.getDBPassword(), configParser.getDBName());


    /*       try {
                UserProfile admin = (UserProfile) xmlReader.readXML("data" + File.separator + "some.xml");
                accountService.addUser(admin.getLogin(), admin.getPassword(), admin.getEmail());
            }
            catch (IOException | ParserConfigurationException | SAXException | NullPointerException e) {
                e.printStackTrace();
                System.out.print("admin not found");
            }

            try {
                UserProfile user = (UserProfile) xmlReader.readXML("data" + File.separator + "user.xml");
                accountService.addUser(user.getLogin(), user.getPassword(), user.getEmail());
            }
            catch (IOException | ParserConfigurationException | SAXException | NullPointerException e) {
                e.printStackTrace();
                System.out.print("user not found");
            }
*/
            AccountService accountService = new AccountService(dbService);
            RoomService roomService = new RoomService();
            UsersReadyToGameService usersReadyToGameService = new UsersReadyToGameService();
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.addServlet(new ServletHolder(new SignInServlet(accountService)), SignInServlet.SIGNIN_PAGE_URL );
            context.addServlet(new ServletHolder(new SignUpServlet(accountService)), SignUpServlet.SIGNUP_PAGE_URL );
            context.addServlet(new ServletHolder(new LogoutServlet(accountService)), LogoutServlet.LOGOUT_PAGE_URL);
            context.addServlet(new ServletHolder(new GameServlet(accountService, roomService)), GameServlet.GAME_PAGE_URL);
            context.addServlet(new ServletHolder(new FindGameServlet(accountService, usersReadyToGameService, roomService)), FindGameServlet.FIND_GAME_URL);
            context.addServlet(new ServletHolder(new AdminServlet(accountService)), AdminServlet.ADMIN_PAGE_URL);
            context.addServlet(new ServletHolder(new GetReadyUserServlet(usersReadyToGameService, accountService)), GetReadyUserServlet.GET_USER_URL);
            context.addServlet(new ServletHolder(new StartNewGame(usersReadyToGameService, accountService, roomService)), StartNewGame.INVITE_URL);
            context.addServlet(new ServletHolder(new GameInfoServlet(accountService, roomService)), GameInfoServlet.GAME_INFO_URL);
            context.addServlet(new ServletHolder(new IAmServlet(accountService)), IAmServlet.I_AM_URL );
            ResourceHandler resourceHandler = new ResourceHandler();
            resourceHandler.setDirectoriesListed(true);
            resourceHandler.setResourceBase("public_html");

            HandlerList handlers = new HandlerList();
            handlers.setHandlers(new Handler[]{resourceHandler, context});

            Server server = new Server(port);
            server.setHandler(handlers);

            try {
                server.start();
            } catch(Exception e) {
                throw new RuntimeException("error", e);
            }
            server.join();
            dbService.shutdown();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException| RuntimeException e){
            System.exit(0);
        }
    }
}
