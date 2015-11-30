package frontend;

import main.*;
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
 * Created by alla on 02.11.15.
 */
public class StartNewGameTest {
    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpServletResponse response = mock(HttpServletResponse.class);
    private static final AccountService ACCOUNT_SERVICE = mock(AccountService.class);
    private static final UsersReadyToGameService USERS_READY_TO_GAME_SERVICER = mock(UsersReadyToGameService.class);
    private static final RoomService ROOM_SERVICE = mock(RoomService.class);
    private StringWriter stringWriter;
    private HttpSession session = mock(HttpSession.class);
    private static final Context INSTANCE = Context.getInstance();
    private StartNewGame startNewGame;
    private UserProfile testUser;
    private final String username = "test_username";

    @BeforeClass
    public  static void before() throws Exception{
        INSTANCE.add(UsersReadyToGameService.class, USERS_READY_TO_GAME_SERVICER);
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
        when(response.getWriter()).thenReturn(writer);
        when(request.getSession()).thenReturn(session);
        String password = "test_password";
        String email = "test_email@mail";
        testUser = new UserProfile(username, password, email);
        startNewGame = new StartNewGame();
    }
    @Test
    public void testDoGetNoUserInRequest() throws IOException, ServletException {
        when(request.getParameter("user")).thenReturn(null);
        startNewGame.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void testDoGetNoUser() throws IOException, ServletException {
        when(request.getParameter("user")).thenReturn(username);
        when(USERS_READY_TO_GAME_SERVICER.checkUser(username)).thenReturn(false);
        startNewGame.doGet(request, response);
        verify(response, never()).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("game_status", false, obj.get("game_status"));

    }

    @Test
    public void testDoGet() throws IOException, ServletException {
        String inviteUsername = "invite_username";
        String invitePassword = "invitePassword";
        String inviteEmail = "inviteEmail@mail.ru";
        UserProfile inviteUser  = new UserProfile(inviteUsername, invitePassword, inviteEmail );
        when(request.getParameter("user")).thenReturn(inviteUsername);
        when(USERS_READY_TO_GAME_SERVICER.checkUser(inviteUsername)).thenReturn(true);
        when(ACCOUNT_SERVICE.getCurrentUser(request.getSession().getId())).thenReturn(testUser);
        when(ACCOUNT_SERVICE.getUser(inviteUsername)).thenReturn(inviteUser);
        startNewGame.doGet(request, response);
        verify(response, never()).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("user_status", "request_user_found", obj.get("user_status"));
    }
}