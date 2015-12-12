package frontend;

import main.*;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

//import org.json.simple.JSONArray;

/**
 * Created by alla on 02.11.15.
 */
public class GetReadyUserServletTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private static AccountService accountService = mock(AccountService.class);
    private static UsersReadyToGameService usersReadyToGameService = mock(UsersReadyToGameService.class);
    private static RoomService roomService = mock(RoomService.class);
    private final HttpSession session = mock(HttpSession.class);
    private final StringWriter stringWriter = new StringWriter();
    final PrintWriter writer = new PrintWriter(stringWriter);
    private GetReadyUserServlet getReadyUserServlet;
    private Set<UserProfile> users_ready;
    private UserProfile testUser;
    private final String username = "test_username";
    private static Context instance = Context.getInstance();
    private final String password = "test_password";
    private final String email = "test_email@mail";

    @BeforeClass
    public static void before(){
        instance.add(UsersReadyToGameService.class, usersReadyToGameService);
        instance.add(RoomService.class, roomService);
        instance.add(AccountService.class,(accountService));
    }

    @AfterClass
    public static void after() throws Exception {
        instance.remove(UsersReadyToGameService.class);
        instance.remove(RoomService.class);
        instance.remove(AccountService.class);
    }


    @Before
    public void initialization() throws Exception {
        when(response.getWriter()).thenReturn(writer);
        when(request.getSession()).thenReturn(session);
        getReadyUserServlet = new GetReadyUserServlet();
        users_ready = new HashSet<>();
        testUser = new UserProfile(username, password, email);
    }

    @Test
    public void testDoGetAnonimus() throws Exception {
        users_ready.add(testUser);
        when(usersReadyToGameService.getUserReady()).thenReturn(users_ready);
        when(accountService.checkSeassions(request.getSession().getId())).thenReturn(false);
        getReadyUserServlet.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(response, never()).setStatus(HttpServletResponse.SC_OK);

    }

    @Test
    public void testDo() throws Exception {
        users_ready.add(testUser);
        when(usersReadyToGameService.getUserReady()).thenReturn(users_ready);
        when(accountService.getCurrentUser(request.getSession().getId())).thenReturn(testUser);
        when(accountService.checkSeassions(request.getSession().getId())).thenReturn(true);
        getReadyUserServlet.doGet(request, response);
    }

}