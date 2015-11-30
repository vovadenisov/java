package frontend;

import main.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by alla on 24.10.15.
 */


public class SignUpServletTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private static final AccountService ACCOUNT_SERVICE = mock(AccountService.class);
    private static final UsersReadyToGameService USERS_READY_TO_GAME_SERVICE = mock(UsersReadyToGameService.class);
    private static final RoomService ROOM_SERVICE = mock(RoomService.class);
    private final HttpSession session = mock(HttpSession.class);
    private static final Context INSTANCE = Context.getInstance();
    private final String username = "test_username";
    private final String password = "test_password";
    private final String email = "test_email@mail";
    private StringWriter stringWriter;
    private SignUpServlet signUp;

    @BeforeClass
    public static void before(){
        INSTANCE.add(UsersReadyToGameService.class, USERS_READY_TO_GAME_SERVICE);
        INSTANCE.add(RoomService.class, ROOM_SERVICE);
        INSTANCE.add(AccountService.class, ACCOUNT_SERVICE);
    }

    @AfterClass
    public static void after() throws Exception {
        INSTANCE.remove(UsersReadyToGameService.class);
        INSTANCE.remove(RoomService.class);
        INSTANCE.remove(AccountService.class);
    }

    @Before
    public void initialization() throws IOException {
        stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(request.getParameter("name")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("email")).thenReturn(email);
        when(request.getSession()).thenReturn(session);
        when(response.getWriter()).thenReturn(writer);
        signUp = new SignUpServlet();
    }
    @Test
    public void testDoPostAlreadyExist() throws IOException, ServletException, JSONException {
        when(ACCOUNT_SERVICE.addUser(username, password, email)).thenReturn(false);
        signUp.doPost(request, response);
        verify(request).getParameter("name");
        verify(request).getParameter("password");
        verify(request).getParameter("email");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("testDoPostAlreadyExist(). Wrong username", username, obj.get("login"));
        assertEquals("testDoPostAlreadyExist(). Wrong password", password, obj.get("password"));
        assertEquals("testDoPostAlreadyExist(). Wrong status", 200, obj.get("status"));
        assertEquals("testDoPostAlreadyExist(). Wrong login status", "false", obj.get("login_status"));
    }

    @Test
    public void testDoPostSucsesst() throws IOException, ServletException, JSONException {
        when(ACCOUNT_SERVICE.addUser(username, password, email)).thenReturn(true);
        signUp.doPost(request, response);
        verify(request).getParameter("name");
        verify(request).getParameter("password");
        verify(request).getParameter("email");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("testDoPostAlreadyExist(). Wrong username", username, obj.get("login"));
        assertEquals("testDoPostAlreadyExist(). Wrong password", password, obj.get("password"));
        assertEquals("testDoPostAlreadyExist(). Wrong status", 200, obj.get("status"));
        assertEquals("testDoPostAlreadyExist(). Wrong login status", "true", obj.get("login_status"));
    }
}
