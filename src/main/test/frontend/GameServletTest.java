package frontend;


import main.AccountService;
import org.junit.Before;
import org.junit.Test;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import main.UserProfile;
import static org.junit.Assert.*;
import main.RoomService;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

import static org.mockito.Mockito.mock;

/**
 * Created by alla on 02.11.15.
 */

public class GameServletTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AccountService accountService = mock(AccountService.class);
    private final RoomService roomService = mock(RoomService.class);
    private final HttpSession session = mock(HttpSession.class);
    private final StringWriter stringWriter = new StringWriter();
    final PrintWriter writer = new PrintWriter(stringWriter);
    private GameServlet gameServlet;

    @Before
    public void initialization() throws Exception {
        when(response.getWriter()).thenReturn(writer);
        when(request.getSession()).thenReturn(session);
        gameServlet = new GameServlet(accountService, roomService);
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
