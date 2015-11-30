package frontend;

import main.*;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;

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
 * Created by alla on 03.11.15.
 */
public class FindGameServletTest {
    private UserProfile testUser;
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private static final AccountService ACCOUNT_SERVICE = mock(AccountService.class);
    private static final UsersReadyToGameService USERS_READY_TO_GAME_SERVICE = mock(UsersReadyToGameService.class);
    private static final RoomService ROOM_SERVICE = mock(RoomService.class);
    private static final Context INSTANCE = Context.getInstance();
    private final HttpSession session = mock(HttpSession.class);
    private StringWriter stringWriter;
    private static FindGameServlet findGameServlet;

    @BeforeClass
    public static void before(){
        INSTANCE.add(UsersReadyToGameService.class, USERS_READY_TO_GAME_SERVICE);
        INSTANCE.add(RoomService.class, ROOM_SERVICE);
        INSTANCE.add(AccountService.class, ACCOUNT_SERVICE);
        findGameServlet = new FindGameServlet();

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
        when(request.getSession()).thenReturn(session);
        when(response.getWriter()).thenReturn(writer);
        String username = "test_username";
        String password = "test_password";
        String email = "test_email@mail";
        testUser = new UserProfile(username, password, email);
    }
    @Test
    public void testDoGetAnonim() throws IOException, ServletException {
        when(ACCOUNT_SERVICE.checkSeassions(request.getSession().getId())).thenReturn(false);
        findGameServlet.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_FOUND);
        verify(response, never()).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testDoGetIsGameUserInRoomTrue() throws IOException, ServletException {
        when(ACCOUNT_SERVICE.checkSeassions(request.getSession().getId())).thenReturn(true);
        when(request.getParameter("is_game")).thenReturn("game");
        when(ACCOUNT_SERVICE.getCurrentUser(request.getSession().getId())).thenReturn(testUser);
        when(ROOM_SERVICE.userInRoom(testUser)).thenReturn(true);
        findGameServlet.doGet(request, response);
        verify(response, never()).setStatus(HttpServletResponse.SC_FOUND);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("game_status", 1, obj.get("game_status"));
    }
    @Test
    public void testDoGetIsGameUserInRoomFalse() throws IOException, ServletException {
        when(ACCOUNT_SERVICE.checkSeassions(request.getSession().getId())).thenReturn(true);
        when(request.getParameter("is_game")).thenReturn("game");
        when(ACCOUNT_SERVICE.getCurrentUser(request.getSession().getId())).thenReturn(testUser);
        when(ROOM_SERVICE.userInRoom(testUser)).thenReturn(false);
        findGameServlet.doGet(request, response);
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("game_status", 0, obj.get("game_status"));
    }
    @Test
    public void testDoPostAnonim() throws IOException, ServletException {
        when(ACCOUNT_SERVICE.checkSeassions(request.getSession().getId())).thenReturn(false);
        findGameServlet.doPost(request, response);
        verify(response).setHeader("location", SignInServlet.SIGNIN_PAGE_URL);
        verify(response, never()).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testDoPostSuccsess() throws IOException, ServletException {
        when(ACCOUNT_SERVICE.checkSeassions(request.getSession().getId())).thenReturn(true);
        when(ACCOUNT_SERVICE.getCurrentUser(request.getSession().getId())).thenReturn(testUser);
        findGameServlet.doPost(request, response);
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("status", "OK", obj.get("status"));
    }
}