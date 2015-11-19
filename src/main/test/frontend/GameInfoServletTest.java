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
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by alla on 03.11.15.
 */
public class GameInfoServletTest {
    private UserProfile testUser;
    private UserProfile enemyUser;
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AccountService accountService = mock(AccountService.class);
    private final HttpSession session = mock(HttpSession.class);
    private final RoomService roomService = mock(RoomService.class);
    private final Room room = mock(Room.class);
    private final UsersReadyToGameService usersReadyToGameService = mock(UsersReadyToGameService.class);
    private final String username = "test_username";
    private final String password = "test_password";
    private final String email = "test_email@mail";
    private final Integer id = 1;
    private final String enemyUsername = "enemy_username";
    private final String enemyPassword = "enemy_password";
    private final String enemyEmail = "enemy_email@mail";
    private final Integer enemyId = 2;
    private final Integer idRoom = 1;
    private final StringWriter stringWriter = new StringWriter();
    final PrintWriter writer = new PrintWriter(stringWriter);
    private GameInfoServlet gameInfoServlet;
    private Set<UserProfile> enemy_user;

    @Before
    public void initialization() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(response.getWriter()).thenReturn(writer);
        gameInfoServlet = new GameInfoServlet(accountService, roomService);
        testUser = new UserProfile(username, password, email, id);
        enemyUser = new UserProfile(enemyUsername, enemyPassword, enemyEmail, enemyId);
        enemy_user = new HashSet<>();
        enemy_user.add(enemyUser);
    }
    @Test
    public void testDoGetNoUserInRequest() throws Exception {
        when(accountService.getCurrentUser(request.getSession().getId())).thenReturn(null);
        when(roomService.userInRoom(testUser)).thenReturn(false);
        gameInfoServlet.doGet(request, response);
        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
    @Test
    public void testDoGetNoRoom() throws Exception {
        when(accountService.getCurrentUser(request.getSession().getId())).thenReturn(testUser);
        when(roomService.userInRoom(testUser)).thenReturn(false);
        gameInfoServlet.doGet(request, response);
        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
    @Test
    public void testDoGetRoomExist() throws Exception {
        when(accountService.getCurrentUser(request.getSession().getId())).thenReturn(testUser);
        when(roomService.userInRoom(testUser)).thenReturn(true);
        when(roomService.getRoomWithUser(testUser)).thenReturn(idRoom);
        when(roomService.getRoom(idRoom)).thenReturn(room);
        when(roomService.getRoom(idRoom).getEnemyTeamUsers(testUser)).thenReturn(enemy_user);
        gameInfoServlet.doGet(request, response);
        verify(response).setContentType("application/json");
        verify(response, never()).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("room_id", idRoom, obj.get("room_id"));
    }
}