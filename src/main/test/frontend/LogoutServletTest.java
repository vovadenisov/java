package frontend;

import main.AccountService;
import main.Context;
import main.RoomService;
import main.UsersReadyToGameService;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by alla on 25.10.15.
 */

    public class LogoutServletTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private static AccountService accountService = mock(AccountService.class);
    private static UsersReadyToGameService usersReadyToGameService = mock(UsersReadyToGameService.class);
    private static RoomService roomService = mock(RoomService.class);
    private static Context instance = Context.getInstance();
    private final HttpSession session = mock(HttpSession.class);
    private final StringWriter stringWriter = new StringWriter();
    final PrintWriter writer = new PrintWriter(stringWriter);
    private LogoutServlet logOut;
    private final String username = "test_username";
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
        when(request.getSession()).thenReturn(session);
        when(response.getWriter()).thenReturn(writer);
        logOut = new LogoutServlet();
    }

    @Test
    public void testDoGetSuccess() throws Exception {
        when(accountService.userSession(request.getRequestedSessionId())).thenReturn(username);
        when(accountService.removeSeassions(request.getSession().getId())).thenReturn(true);
        logOut.doGet(request, response);
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("testDoGetSuccess(). Wrong username", username, obj.get("login"));
        assertEquals("testDoGetSuccess(). Wrong status", 200, obj.get("status"));
        assertEquals("testDoGetSuccess(). Wrong logOut status", true, obj.get("logout_status"));
        assertEquals("testDoGetSuccess(). Wrong error message", "correct", obj.get("error_message"));
    }
    @Test
    public void testDoGetFail() throws Exception {
        when(accountService.userSession(request.getRequestedSessionId())).thenReturn("");
        logOut.doGet(request, response);
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("testDoGetSuccess(). Wrong username", "anonim", obj.get("login"));
        assertEquals("testDoGetSuccess(). Wrong status", 200, obj.get("status"));
        assertEquals("testDoGetSuccess(). Wrong logOut status", false, obj.get("logout_status"));
        assertEquals("testDoGetSuccess(). Wrong error message", "incorrect", obj.get("error_message"));
    }
}