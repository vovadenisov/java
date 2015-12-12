package frontend;

import main.*;
import netscape.javascript.JSException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
 * Created by alla on 23.10.15.
 */


public class SignInServletTest {
    private UserProfile testUser;
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private static final AccountService ACCOUNT_SERVICE = mock(AccountService.class);
    private static final UsersReadyToGameService USERS_READY_TO_GAME_SERVICE = mock(UsersReadyToGameService.class);
    private static final RoomService ROOM_SERVICE = mock(RoomService.class);private final HttpSession session = mock(HttpSession.class);
    private static final Context INSTANCE = Context.getInstance();
    private final String username = "test_username";
    private final String password = "test_password";
    private StringWriter stringWriter;
    private SignInServlet signIn;

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
        String email = "test_email@mail";
        when(request.getParameter("email")).thenReturn(email);
        when(request.getSession()).thenReturn(session);
        when(response.getWriter()).thenReturn(writer);
        signIn = new SignInServlet();
        testUser = new UserProfile(username, password, email);
    }
    @Test
    public void testDoGetAnonim() throws IOException, ServletException {
        when(ACCOUNT_SERVICE.checkSeassions(request.getSession().getId())).thenReturn(false);
        signIn.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(ACCOUNT_SERVICE).getCurrentUser(request.getSession().getId());
    }
    @Test
    public void testDoGetAlreadyLoggedUser() throws IOException, JSONException, ServletException {
        when(ACCOUNT_SERVICE.checkSeassions(request.getSession().getId())).thenReturn(true);
        when(ACCOUNT_SERVICE.getCurrentUser(request.getSession().getId())).thenReturn(testUser);
        signIn.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(ACCOUNT_SERVICE).getCurrentUser(request.getSession().getId());
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("username", username, obj.get("login"));
        assertEquals("password", password, obj.get("password"));
        assertEquals("username", 200, obj.get("status"));
        assertEquals("username", false, obj.get("login_status"));
        assertEquals("username", "Already logged", obj.get("error_massage"));
    }
    @Test
    public void testDoPostAlreadyLoggedUser() throws IOException, JSONException, ServletException {
        when(ACCOUNT_SERVICE.checkUser(username)).thenReturn(true);
        when(ACCOUNT_SERVICE.checkUserlogin(testUser)).thenReturn(true);
        when(ACCOUNT_SERVICE.getUser(username)).thenReturn(testUser);
        signIn.doPost(request, response);
        verify(request).getParameter("name");
        verify(request).getParameter("password");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(request, never()).setAttribute("user", testUser);
        verify(ACCOUNT_SERVICE, never()).addSessions(request.getSession().getId(), testUser);
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("testDoPostAlreadyLoggedUser. Checking username", username, obj.get("login"));
        assertEquals("testDoPostAlreadyLoggedUser. Checking password", password, obj.get("password"));
        assertEquals("testDoPostAlreadyLoggedUser. Checking status", 200, obj.get("status"));
        assertEquals("testDoPostAlreadyLoggedUser. Checking login status", false, obj.get("login_status"));
        assertEquals("testDoPostAlreadyLoggedUser. Checking error massage", "already logged", obj.get("error_massage"));
    }

    @Test
    public void testDoPostDoesNotExist() throws IOException, JSONException, ServletException {
        when(ACCOUNT_SERVICE.checkUser(username)).thenReturn(false);
        signIn.doPost(request, response);
        verify(request).getParameter("name");
        verify(request).getParameter("password");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(ACCOUNT_SERVICE, never()).checkUserlogin(testUser);
        verify(request, never()).setAttribute("user", testUser);
        verify(ACCOUNT_SERVICE, never()).addSessions(request.getSession().getId(), testUser);
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("username", username, obj.get("login"));
        assertEquals("password", password, obj.get("password"));
        assertEquals("status", 200, obj.get("status"));
        assertEquals("login_status1", false, obj.get("login_status"));
        assertEquals("username", "User with this name does not exist", obj.get("error_massage"));
    }
    @Test
    public void testDoPostWrongLoginPassword() throws IOException, JSONException, ServletException {
        when(ACCOUNT_SERVICE.checkUser(username)).thenReturn(true);
        when(ACCOUNT_SERVICE.checkUserlogin(testUser)).thenReturn(false);
        when(request.getParameter("password")).thenReturn("wrong_password");
        when(ACCOUNT_SERVICE.getUser(username)).thenReturn(testUser);
        signIn.doPost(request, response);
        verify(request).getParameter("name");
        verify(request).getParameter("password");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(request, never()).setAttribute("user", testUser);
        verify(ACCOUNT_SERVICE, never()).addSessions(request.getSession().getId(), testUser);
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("username", username, obj.get("login"));
        assertEquals("status", 200, obj.get("status"));
        assertEquals("login_status2", obj.get("login_status"), false);
        assertEquals("username", "Wrong login/password", obj.get("error_massage"));
    }
    @Test
    public void testDoPostSucsess() throws IOException, JSONException, ServletException {
        when(ACCOUNT_SERVICE.checkUser(username)).thenReturn(true);
        when(ACCOUNT_SERVICE.checkUserlogin(testUser)).thenReturn(false);
        when(ACCOUNT_SERVICE.getUser(username)).thenReturn(testUser);
        signIn.doPost(request, response);
        verify(request).getParameter("name");
        verify(request).getParameter("password");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(request).setAttribute("user", testUser);
        verify(ACCOUNT_SERVICE).addSessions(request.getSession().getId(), testUser);
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("username", username, obj.get("login"));
        assertEquals("password", password, obj.get("password"));
        assertEquals("status", 200, obj.get("status"));
        assertEquals("login_status3", true, obj.get("login_status"));
        assertEquals("username", "", obj.get("error_massage"));
    }
}
