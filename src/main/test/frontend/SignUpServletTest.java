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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by alla on 24.10.15.
 */

/**
public class SignUpServletTest {
    private UserProfile testUser;
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final AccountService accountService = mock(AccountService.class);
    private final HttpSession session = mock(HttpSession.class);
    private final String username = "test_username";
    private final String password = "test_password";
    private final String email = "test_email@mail";
    private final StringWriter stringWriter = new StringWriter();
    final PrintWriter writer = new PrintWriter(stringWriter);
    private SignUpServlet signUp;

    @Before
    public void initialization() throws Exception {
        when(request.getParameter("name")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("email")).thenReturn(email);
        when(request.getSession()).thenReturn(session);
        when(response.getWriter()).thenReturn(writer);
        signUp = new SignUpServlet(accountService);
        testUser = new UserProfile(username, password, email);
    }
    @Test
    public void testDoPostAlreadyExist() throws Exception {
        when(accountService.addUser(username, testUser)).thenReturn(false);
        signUp.doPost(request, response);
        verify(request).getParameter("name");
        verify(request).getParameter("password");
        verify(request).getParameter("email");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("testDoPostAlreadyExist(). Wrong username", username, obj.get("login"));
        assertEquals("testDoPostAlreadyExist(). Wrong password", password, obj.get("password"));
        assertEquals("testDoPostAlreadyExist(). Wrong email", email, obj.get("email"));
        assertEquals("testDoPostAlreadyExist(). Wrong status", 200, obj.get("status"));
        assertEquals("testDoPostAlreadyExist(). Wrong login status", false, obj.get("login_status"));
        assertEquals("testDoPostAlreadyExist(). Wrong error message", "User with name: " + username + " already exists", obj.get("error_massage"));
    }

    @Test
    public void testDoPostSucsesst() throws Exception {
        when(accountService.addUser(username, testUser)).thenReturn(true);
        signUp.doPost(request, response);
        verify(request).getParameter("name");
        verify(request).getParameter("password");
        verify(request).getParameter("email");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("testDoPostAlreadyExist(). Wrong username", username, obj.get("login"));
        assertEquals("testDoPostAlreadyExist(). Wrong password", password, obj.get("password"));
        assertEquals("testDoPostAlreadyExist(). Wrong email", email, obj.get("email"));
        assertEquals("testDoPostAlreadyExist(). Wrong status", 200, obj.get("status"));
        assertEquals("testDoPostAlreadyExist(). Wrong login status", true, obj.get("login_status"));
        assertEquals("testDoPostAlreadyExist(). Wrong error message", "New user created name: " + username, obj.get("error_massage"));
    }

    @Test
    public void testDoGetAlreadyExist() throws Exception {
        when(accountService.checkSeassions(request.getSession().getId())).thenReturn(true);
        when(accountService.getCurrentUser(request.getSession().getId())).thenReturn(testUser);
        signUp.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        System.out.println(stringWriter.toString());
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("testDoPostAlreadyExist(). Wrong username", username, obj.get("login"));
        assertEquals("testDoPostAlreadyExist(). Wrong password", password, obj.get("password"));
        assertEquals("testDoPostAlreadyExist(). Wrong email", email, obj.get("email"));
        assertEquals("testDoPostAlreadyExist(). Wrong status", 200, obj.get("status"));
        assertEquals("testDoPostAlreadyExist(). Wrong login status", false, obj.get("login_status"));
        assertEquals("testDoPostAlreadyExist(). Wrong error message", "User with name: " + username + " already exists", obj.get("error_massage"));
    }
    public void testDoGetSucsesst() throws Exception {
        when(accountService.checkSeassions(request.getSession().getId())).thenReturn(false);
        signUp.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }
}
 */