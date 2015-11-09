package frontend;

import main.AccountService;
import main.UserProfile;
import main.UsersReadyToGameService;
import org.junit.Before;
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
    private final AccountService accountService = mock(AccountService.class);
    private final HttpSession session = mock(HttpSession.class);
    private final UsersReadyToGameService usersReadyToGameService = mock(UsersReadyToGameService.class);
    private final StringWriter stringWriter = new StringWriter();
    final PrintWriter writer = new PrintWriter(stringWriter);
    private GetReadyUserServlet getReadyUserServlet;
    private Set<UserProfile> users_ready;
    private UserProfile testUser;
    private final String username = "test_username";
    private final String password = "test_password";
    private final String email = "test_email@mail";
    private final Integer id = 1;
    @Before
    public void initialization() throws Exception {
        when(response.getWriter()).thenReturn(writer);
        when(request.getSession()).thenReturn(session);
        getReadyUserServlet = new GetReadyUserServlet(usersReadyToGameService, accountService);
        users_ready = new HashSet<>();
        testUser = new UserProfile(username, password, email, id);
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
        verify(response, never()).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");

    }

}