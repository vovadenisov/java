package frontend;

import main.*;
import netscape.javascript.JSException;
import org.json.JSONObject;
import org.junit.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by alla on 25.10.15.
 */
public class AdminServletTest {
    private static final HttpServletRequest HTTP_SERVLET_REQUEST = mock(HttpServletRequest.class);
    private static final HttpServletResponse HTTP_SERVLET_RESPONSE = mock(HttpServletResponse.class);
    private static final AccountService ACCOUNT_SERVICE = mock(AccountService.class);
    private static final UsersReadyToGameService USERS_READY_TO_GAME_SERVICE = mock(UsersReadyToGameService.class);
    private static final RoomService ROOM_SERVICE = mock(RoomService.class);
    private static final Context INSTANCE = Context.getInstance();
    private static final HttpSession SESSION = mock(HttpSession.class);
    private StringWriter stringWriter;
    private static  AdminServlet adminServlet;

    @BeforeClass
    public static void before() throws Exception{
        INSTANCE.add(UsersReadyToGameService.class, (USERS_READY_TO_GAME_SERVICE));
        INSTANCE.add(RoomService.class, (ROOM_SERVICE));
        INSTANCE.add(AccountService.class, (ACCOUNT_SERVICE));
        adminServlet = new AdminServlet();
    }
    @Before
    public void initialization() throws IOException {
        stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(HTTP_SERVLET_REQUEST.getSession()).thenReturn(SESSION);
        when(HTTP_SERVLET_RESPONSE.getWriter()).thenReturn(printWriter);
    }

    @AfterClass
    public static void after() throws Exception {
        INSTANCE.remove(UsersReadyToGameService.class);
        INSTANCE.remove(RoomService.class);
        INSTANCE.remove(AccountService.class);
    }
    @Test
    public void testDoGet() throws IOException, JSException, ServletException {
        when(HTTP_SERVLET_REQUEST.getParameter("shutdown")).thenReturn(null);
        when(ACCOUNT_SERVICE.numberOfRegistered()).thenReturn(2);
        when(ACCOUNT_SERVICE.numberOfSessions()).thenReturn(1);
        adminServlet.doGet(HTTP_SERVLET_REQUEST, HTTP_SERVLET_RESPONSE);
        verify(ACCOUNT_SERVICE).numberOfRegistered();
        verify(ACCOUNT_SERVICE).numberOfSessions();
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("Incorrect number of registered users", 2, obj.get("registered"));
        assertEquals("Incorrect number of online users", 1, obj.get("online"));
        assertEquals("Wrong status", 200, obj.get("status"));
    }
}