package main;

import database.dbServise.DBService;
import exceptions.ConfigException;
import frontend.*;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;;
import parser.ConfigParser;

/**
 * Created by alla edited by nastya on 16.09.15.
 *
 */

public class Main {

    public static void main(String[] args) throws NumberFormatException {
        try {
            ConfigParser configParser = new ConfigParser();
            Integer portString = configParser.getPort();
            int port = portString;
            System.out.append("Starting at port: ").append(portString.toString()).append('\n');
            DBService dbService = new DBService(configParser);
            AccountService accountService = new AccountService(dbService);

            Context instance = Context.getInstance();
            instance.add(UsersReadyToGameService.class, new UsersReadyToGameService());
            instance.add(RoomService.class, new RoomService());
            instance.add(AccountService.class, accountService);

            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

            context.addServlet(new ServletHolder(new SignInServlet()), SignInServlet.SIGNIN_PAGE_URL);
            context.addServlet(new ServletHolder(new SignUpServlet()), SignUpServlet.SIGNUP_PAGE_URL);
            context.addServlet(new ServletHolder(new LogoutServlet()), LogoutServlet.LOGOUT_PAGE_URL);
            context.addServlet(new ServletHolder(new GameServlet()), GameServlet.GAME_PAGE_URL);
            context.addServlet(new ServletHolder(new FindGameServlet()), FindGameServlet.FIND_GAME_URL);
            context.addServlet(new ServletHolder(new AdminServlet()), AdminServlet.ADMIN_PAGE_URL);
            context.addServlet(new ServletHolder(new GetReadyUserServlet()), GetReadyUserServlet.GET_USER_URL);
            context.addServlet(new ServletHolder(new StartNewGame()), StartNewGame.INVITE_URL);
            context.addServlet(new ServletHolder(new GameInfoServlet()), GameInfoServlet.GAME_INFO_URL);
            context.addServlet(new ServletHolder(new IAmServlet()), IAmServlet.I_AM_URL);

            ResourceHandler resourceHandler = new ResourceHandler();
            resourceHandler.setDirectoriesListed(true);
            resourceHandler.setResourceBase("public_html");

            HandlerList handlers = new HandlerList();
            handlers.setHandlers(new Handler[]{resourceHandler, context});

            Server server = new Server(port);
            server.setHandler(handlers);

            try {
                server.start();
            } catch (Exception e) {
                throw new RuntimeException("error", e);
            }
            server.join();
            dbService.shutdown();
        }
        catch (ConfigException e){
            System.out.println(e.getMessage());
            System.exit(0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }
    }
}
