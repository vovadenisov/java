package main;

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
import java.io.File;
import java.io.IOException;


/**
 * Created by alla edited by nastya on 16.09.15.
 *
 */

public class Main {
    public static void main(String[] args) throws NumberFormatException, InterruptedException, IOException {
        try {
            ConfigParser configParser = new ConfigParser();
            String portString = configParser.getPort();
            int port = Integer.valueOf(portString);
            System.out.append("Starting at port: ").append(portString).append('\n');
            XMLReader xmlReader = new XMLReader();

            AccountService accountService = new AccountService();
            Context instance = Context.getInstance();
            instance.add(UsersReadyToGameService.class, (Object)(new UsersReadyToGameService()));
            instance.add(RoomService.class, (Object)(new RoomService()));
            instance.add(AccountService.class, (Object)(accountService));

            try {
                UserProfile admin = (UserProfile) xmlReader.readXML("data" + File.separator + "some.xml");
                accountService.addUser(admin.getLogin(), admin.getPassword(), admin.getEmail());
            }
            catch (IOException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
                System.out.print("admin not found");
            }

            try {
                UserProfile user = (UserProfile) xmlReader.readXML("data" + File.separator + "user.xml");
                accountService.addUser(user.getLogin(), user.getPassword(), user.getEmail());
            }
            catch (IOException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
                System.out.print("user not found");
            }
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.addServlet(new ServletHolder(new SignInServlet()), SignInServlet.SIGNIN_PAGE_URL );
            context.addServlet(new ServletHolder(new SignUpServlet()), SignUpServlet.SIGNUP_PAGE_URL );
            context.addServlet(new ServletHolder(new LogoutServlet()), LogoutServlet.LOGOUT_PAGE_URL);
            context.addServlet(new ServletHolder(new GameServlet()), GameServlet.GAME_PAGE_URL);
            context.addServlet(new ServletHolder(new FindGameServlet()), FindGameServlet.FIND_GAME_URL);
            context.addServlet(new ServletHolder(new AdminServlet()), AdminServlet.ADMIN_PAGE_URL);
            context.addServlet(new ServletHolder(new GetReadyUserServlet()), GetReadyUserServlet.GET_USER_URL);
            context.addServlet(new ServletHolder(new StartNewGame()), StartNewGame.INVITE_URL);
            context.addServlet(new ServletHolder(new GameInfoServlet()), GameInfoServlet.GAME_INFO_URL);
            context.addServlet(new ServletHolder(new IAmServlet()), IAmServlet.I_AM_URL );
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
        }
        catch (IOException e){
            e.printStackTrace();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
