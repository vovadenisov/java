package frontend;

import main.*;
import org.json.JSONObject;
import org.junit.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private static AccountService accountService = mock(AccountService.class);
    private final HttpSession session = mock(HttpSession.class);
    private static RoomService roomService = mock(RoomService.class);
    private final Room room = mock(Room.class);
    private static UsersReadyToGameService usersReadyToGameService = mock(UsersReadyToGameService.class);
    private static Context instance = Context.getInstance();
    private final StringWriter stringWriter = new StringWriter();
    final PrintWriter writer = new PrintWriter(stringWriter);
    private static GameInfoServlet gameInfoServlet;
    private Set<UserProfile> enemy_user;

    @BeforeClass
    public static void before(){
        instance.add(UsersReadyToGameService.class, usersReadyToGameService);
        instance.add(RoomService.class, roomService);
        instance.add(AccountService.class, accountService);
        gameInfoServlet = new GameInfoServlet();
    }


    @Before
    public void initialization() throws IOException {
        when(request.getSession()).thenReturn(session);
        when(response.getWriter()).thenReturn(writer);
        String username = "test_username";
        String password = "test_password";
        String email = "test_email@mail";
        testUser = new UserProfile(username, password, email);
        String enemyUsername = "enemy_username";
        String enemyPassword = "enemy_password";
        String enemyEmail = "enemy_email@mail";
        UserProfile enemyUser = new UserProfile(enemyUsername, enemyPassword, enemyEmail);
        enemy_user = new HashSet<>();
        enemy_user.add(enemyUser);
    }

    @AfterClass
    public static void after() throws Exception {
        instance.remove(UsersReadyToGameService.class);
        instance.remove(RoomService.class);
        instance.remove(AccountService.class);
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
        Integer idRoom = 1;
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