package frontend;

import main.AccountService;
import main.RoomService;
import main.UserProfile;
import main.UsersReadyToGameService;
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
 * Created by alla on 03.11.15.
 */
public class FindGameServletTest {
    private UserProfile testUser;
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AccountService accountService = mock(AccountService.class);
    private final HttpSession session = mock(HttpSession.class);
    private final RoomService roomService = mock(RoomService.class);
    private final UsersReadyToGameService usersReadyToGameService = mock(UsersReadyToGameService.class);
    private final String username = "test_username";
    private final String password = "test_password";
    private final String email = "test_email@mail";
    private final Integer id = 1;
    private final StringWriter stringWriter = new StringWriter();
    final PrintWriter writer = new PrintWriter(stringWriter);
    private FindGameServlet findGameServlet;

    @Before
    public void initialization() throws Exception {
        findGameServlet = new FindGameServlet(accountService, usersReadyToGameService, roomService);
        when(request.getSession()).thenReturn(session);
        when(response.getWriter()).thenReturn(writer);
        testUser = new UserProfile(username, password, email);
    }
    @Test
    public void testDoGetAnonim() throws Exception {
        when(accountService.checkSeassions(request.getSession().getId())).thenReturn(false);
        findGameServlet.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_FOUND);
        verify(response, never()).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testDoGetIsGameUserInRoomTrue() throws Exception {
        when(accountService.checkSeassions(request.getSession().getId())).thenReturn(true);
        when(request.getParameter("is_game")).thenReturn("game");
        when(accountService.getCurrentUser(request.getSession().getId())).thenReturn(testUser);
        when(roomService.userInRoom(testUser)).thenReturn(true);
        findGameServlet.doGet(request, response);
        verify(response, never()).setStatus(HttpServletResponse.SC_FOUND);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("game_status", 1, obj.get("game_status"));
    }
    @Test
    public void testDoGetIsGameUserInRoomFalse() throws Exception {
        when(accountService.checkSeassions(request.getSession().getId())).thenReturn(true);
        when(request.getParameter("is_game")).thenReturn("game");
        when(accountService.getCurrentUser(request.getSession().getId())).thenReturn(testUser);
        when(roomService.userInRoom(testUser)).thenReturn(false);
        findGameServlet.doGet(request, response);
        verify(response, never()).setStatus(HttpServletResponse.SC_FOUND);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("game_status", 0, obj.get("game_status"));
    }
    @Test
    public void testDoPostAnonim() throws Exception {
        when(accountService.checkSeassions(request.getSession().getId())).thenReturn(false);
        findGameServlet.doPost(request, response);
        verify(response).setHeader("location", SignInServlet.SIGNIN_PAGE_URL);
        verify(response, never()).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testDoPostSuccsess() throws Exception {
        when(accountService.checkSeassions(request.getSession().getId())).thenReturn(true);
        when(accountService.getCurrentUser(request.getSession().getId())).thenReturn(testUser);
        findGameServlet.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(usersReadyToGameService).addUserToReady(testUser);
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("status", "OK", obj.get("status"));
    }
}