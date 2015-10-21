package main;

import frontend.AdminServlet;
import frontend.LogoutServlet;
import frontend.SignInServlet;
import frontend.SignUpServlet;
import frontend.GameServlet;
import frontend.FindGameServlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Created by alla edited by nastya on 16.09.15.
 *
 */

public class Main {
    public static void main(String[] args) throws NumberFormatException, InterruptedException {
        if (args.length != 1) {
            System.out.append("Use port as the first argument");
            System.exit(1);

        }
        String portString = args[0];
        int port = Integer.valueOf(portString);
        System.out.append("Starting at port: ").append(portString).append('\n');

        AccountService accountService = new AccountService();
        accountService.addUser("Admin", new UserProfile("Admin", "1234", "admin@mail.ru"));
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new SignInServlet(accountService)), SignInServlet.SIGNIN_PAGE_URL );
        context.addServlet(new ServletHolder(new SignUpServlet(accountService)), SignUpServlet.SIGNUP_PAGE_URL );
        context.addServlet(new ServletHolder(new LogoutServlet(accountService)), LogoutServlet.LOGOUT_PAGE_URL);
        context.addServlet(new ServletHolder(new LogoutServlet(accountService)), GameServlet.GAME_PAGE_URL);
        context.addServlet(new ServletHolder(new LogoutServlet(accountService)), FindGameServlet.FIND_GAME_URL);
        context.addServlet(new ServletHolder(new AdminServlet(accountService)), AdminServlet.ADMIN_PAGE_URL);
        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setDirectoriesListed(true);
        resource_handler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        Server server = new Server(port);
        server.setHandler(handlers);

        try {
            server.start();
        } catch(Exception e) {
            throw new RuntimeException("error", e);
        }
        server.join();
    }
}
