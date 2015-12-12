package frontend;


import main.AccountService;
import main.Context;
import main.RoomService;
import main.UsersReadyToGameService;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;

/**
 * Created by alla on 02.11.15.
 */

public class GameServletTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private static AccountService accountService = mock(AccountService.class);
    private static UsersReadyToGameService usersReadyToGameService = mock(UsersReadyToGameService.class);
    private static RoomService roomService = mock(RoomService.class);
    private static Context instance = Context.getInstance();
    private final HttpSession session = mock(HttpSession.class);
    private final StringWriter stringWriter = new StringWriter();
    final PrintWriter writer = new PrintWriter(stringWriter);
    private GameServlet gameServlet;

    @BeforeClass
    public static void before(){
        instance.add(UsersReadyToGameService.class, usersReadyToGameService);
        instance.add(RoomService.class,roomService);
        instance.add(AccountService.class, (accountService));
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
        gameServlet = new GameServlet();
    }

    @Test
    public void testDoGetNoPush() throws Exception {
        when(request.getParameter("push")).thenReturn(null);
        gameServlet.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(request, never()).getParameter("start");
        verify(request, never()).getParameter("room_id");
    }
    @Test
    public void testDoGetNoRoom() throws Exception {
        when(request.getParameter("room")).thenReturn(null);
        gameServlet.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(request, never()).getParameter("start");
    }

    @Test
    public void testDoGetRoomExistNoStart() throws Exception {
        when(request.getParameter("room")).thenReturn("1");
        when(request.getParameter("start")).thenReturn(null);
        gameServlet.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(accountService, never()).getCurrentUser(request.getSession().getId());

    }
    @Test
    public void testDoGetRoomExistStartExist() throws Exception {
        when(request.getParameter("room")).thenReturn("1");
        when(request.getParameter("start")).thenReturn("start");
        when(accountService.checkSeassions(request.getSession().getId())).thenReturn(true);
        gameServlet.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }
}
