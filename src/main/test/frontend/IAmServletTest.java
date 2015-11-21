package frontend;

import main.AccountService;
import main.UserProfile;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by alla on 02.11.15.
 */
public class IAmServletTest {
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AccountService accountService = mock(AccountService.class);
    private final HttpSession session = mock(HttpSession.class);
    private final StringWriter stringWriter = new StringWriter();
    final PrintWriter writer = new PrintWriter(stringWriter);
    private  IAmServlet iAmServlet;
    private UserProfile testUser;
    private final String username = "test_username";
    private final String password = "test_password";
    private final String email = "test_email@mail";
    private final Integer id = 1;
    @Before
    public void initialization() throws Exception {
        when(response.getWriter()).thenReturn(writer);
        when(request.getSession()).thenReturn(session);
        iAmServlet = new IAmServlet(accountService);
        testUser = new UserProfile(username, password, email);
    }
    @Test
    public void testDoGetFailedSession() throws Exception {
        when(accountService.checkSeassions(request.getSession().getId())).thenReturn(false);
        iAmServlet.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
    @Test
    public void testDoGet() throws Exception {
        when(accountService.checkSeassions(request.getSession().getId())).thenReturn(true);
        when(accountService.getCurrentUser(request.getSession().getId())).thenReturn(testUser);
        iAmServlet.doGet(request, response);
        verify(response, never()).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("current_user", username, obj.get("current_user"));
        assertEquals("current_user_id", id, obj.get("current_user_id"));
    }
}