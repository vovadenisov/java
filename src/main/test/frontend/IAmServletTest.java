package frontend;

import main.*;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by alla on 02.11.15.
 */
public class IAmServletTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private static AccountService accountService = mock(AccountService.class);
    private static UsersReadyToGameService usersReadyToGameService = mock(UsersReadyToGameService.class);
    private static RoomService roomService = mock(RoomService.class);
    private final HttpSession session = mock(HttpSession.class);
    private static Context instance = Context.getInstance();
    private final StringWriter stringWriter = new StringWriter();
    final PrintWriter writer = new PrintWriter(stringWriter);
    private  IAmServlet iAmServlet;
    private UserProfile testUser;
    private final String username = "test_username";
    private final String password = "test_password";
    private final String email = "test_email@mail";

    @BeforeClass
    public static void before(){
        instance.add(UsersReadyToGameService.class, usersReadyToGameService);
        instance.add(RoomService.class, roomService);
        instance.add(AccountService.class, accountService);
    }

    @AfterClass
    public static void after() throws Exception {
        instance.remove(UsersReadyToGameService.class);
        instance.remove(RoomService.class);
        instance.remove(AccountService.class);
    }
    @Before
    public void initialization() throws Exception {
        when(response.getWriter()).thenReturn(writer);
        when(request.getSession()).thenReturn(session);
        iAmServlet = new IAmServlet();
        testUser = new UserProfile(username, password, email);
    }
    @Test
    public void testDoGetFailedSession() throws Exception {
        when(accountService.checkSeassions(request.getSession().getId())).thenReturn(false);
        iAmServlet.doGet(request, response);
    }
    @Test
    public void testDoGet() throws Exception {
        when(accountService.checkSeassions(request.getSession().getId())).thenReturn(true);
        when(accountService.getCurrentUser(request.getSession().getId())).thenReturn(testUser);
        iAmServlet.doGet(request, response);
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("current_user", username, obj.get("current_user"));
    }
}