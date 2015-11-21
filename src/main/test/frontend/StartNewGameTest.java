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
 * Created by alla on 02.11.15.
 */
public class StartNewGameTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AccountService accountService = mock(AccountService.class);
    private final UsersReadyToGameService usersReadyToGameService = mock(UsersReadyToGameService.class);
    private final RoomService roomService = mock(RoomService.class);
    private final StringWriter stringWriter = new StringWriter();
    final PrintWriter writer = new PrintWriter(stringWriter);
    private final HttpSession session = mock(HttpSession.class);
    private final Context instance = Context.getInstance();
    private StartNewGame startNewGame;
    private UserProfile testUser;
    private UserProfile inviteUser;
    private final String username = "test_username";
    private final String password = "test_password";
    private final String email = "test_email@mail";
    private final Integer id = 1;
    private final String inviteUsername = "invite_username";
    private final String invitePassword = "invite_password";
    private final String inviteEmail = "invite_email@mail";
    private final Integer inviteId = 2;

    @Before
    public void initialization() throws Exception {
        instance.add(UsersReadyToGameService.class, (Object)(usersReadyToGameService));
        instance.add(RoomService.class, (Object)(roomService));
        instance.add(AccountService.class, (Object)(accountService));
        when(response.getWriter()).thenReturn(writer);
        when(request.getSession()).thenReturn(session);
        testUser = new UserProfile(username, password, email, id);
        startNewGame = new StartNewGame();
    }
    @Test
    public void testDoGetNoUserInRequest() throws Exception {
        when(request.getParameter("user")).thenReturn(null);
        startNewGame.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void testDoGetNoUser() throws Exception {
        when(request.getParameter("user")).thenReturn(username);
        when(usersReadyToGameService.checkUser(username)).thenReturn(false);
        startNewGame.doGet(request, response);
        verify(response, never()).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("game_status", false, obj.get("game_status"));

    }

    @Test
    public void testDoGet() throws Exception {
        when(request.getParameter("user")).thenReturn(inviteUsername);
        when(usersReadyToGameService.checkUser(inviteUsername)).thenReturn(true);
        when(accountService.getCurrentUser(request.getSession().getId())).thenReturn(testUser);
        when(accountService.getUser(inviteUsername)).thenReturn(inviteUser);
        startNewGame.doGet(request, response);
        verify(response, never()).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("user_status", "request_user_found", obj.get("user_status"));


    }

}