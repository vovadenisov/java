package frontend;

import main.*;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by alla on 25.10.15.
 */
public class AdminServletTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AccountService accountService = mock(AccountService.class);
    private final UsersReadyToGameService usersReadyToGameService = mock(UsersReadyToGameService.class);
    private final RoomService roomService = mock(RoomService.class);
    private final Context instance = Context.getInstance();
    private final Time time = mock(Time.class);
    private final HttpSession session = mock(HttpSession.class);
    private final StringWriter stringWriter = new StringWriter();
    final PrintWriter writer = new PrintWriter(stringWriter);
    private final String username = "test_username";
    private final String password = "test_password";
    private final String email = "test_email@mail";
    private final Integer id = 1;
    private AdminServlet admin;
    private UserProfile testUser;
    @Before
    public void initialization() throws Exception {
        instance.add(UsersReadyToGameService.class, (Object)(usersReadyToGameService));
        instance.add(RoomService.class, (Object)(roomService));
        instance.add(AccountService.class, (Object)(accountService));
        testUser = new UserProfile(username, password, email, id);
        when(request.getSession()).thenReturn(session);
        when(response.getWriter()).thenReturn(writer);
        admin = new AdminServlet();
    }
    @Test
    public void testDoGet() throws Exception {
        when(request.getParameter("shutdown")).thenReturn(null);
        when(accountService.numberOfRegistered()).thenReturn(2);
        when(accountService.numberOfSessions()).thenReturn(1);
        admin.doGet(request, response);
        verify(accountService).numberOfRegistered();
        verify(accountService).numberOfSessions();
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("Incorrect number of registered users", 2, obj.get("registered"));
        assertEquals("Incorrect number of online users", 1, obj.get("online"));
        assertEquals("Wrong status", 200, obj.get("status"));
    }
}