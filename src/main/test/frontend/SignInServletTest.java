package frontend;

import main.AccountService;
import main.UserProfile;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;
import java.io.PrintWriter;
import org.json.JSONObject;
import java.io.StringWriter;

/**
 * Created by alla on 23.10.15.
 */

/**
public class SignInServletTest {
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
    private SignInServlet signIn;


    @Before
    public void initialization() throws Exception {
        when(request.getParameter("name")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("email")).thenReturn(email);
        when(request.getSession()).thenReturn(session);
        when(response.getWriter()).thenReturn(writer);
        signIn = new SignInServlet(accountService);
        testUser = new UserProfile(username, password, email);
    }
    @Test
    public void testDoGetAnonim() throws Exception {
        when(accountService.checkSeassions(request.getSession().getId())).thenReturn(false);
        signIn.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(accountService, never()).getCurrentUser(request.getSession().getId());
    }
    @Test
    public void testDoGetAlreadyLoggedUser() throws Exception {
        when(accountService.checkSeassions(request.getSession().getId())).thenReturn(true);
        when(accountService.getCurrentUser(request.getSession().getId())).thenReturn(testUser);
        signIn.doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(accountService).getCurrentUser(request.getSession().getId());
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("username", username, obj.get("login"));
        assertEquals("password", password, obj.get("password"));
        assertEquals("username", 200, obj.get("status"));
        assertEquals("username", false, obj.get("login_status"));
        assertEquals("username", "Already logged", obj.get("error_massage"));
    }
    @Test
    public void testDoPostAlreadyLoggedUser() throws Exception {
        when(accountService.checkUser(username)).thenReturn(true);
        when(accountService.checkUserlogin(testUser)).thenReturn(true);
        when(accountService.getUser(username)).thenReturn(testUser);
        signIn.doPost(request, response);
        verify(request).getParameter("name");
        verify(request).getParameter("password");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(request, never()).setAttribute("user", testUser);
        verify(accountService, never()).addSessions(request.getSession().getId(), testUser);
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("testDoPostAlreadyLoggedUser. Checking username", username, obj.get("login"));
        assertEquals("testDoPostAlreadyLoggedUser. Checking password", password, obj.get("password"));
        assertEquals("testDoPostAlreadyLoggedUser. Checking status", 200, obj.get("status"));
        assertEquals("testDoPostAlreadyLoggedUser. Checking login status", false, obj.get("login_status"));
        assertEquals("testDoPostAlreadyLoggedUser. Checking error massage", "already logged", obj.get("error_massage"));
    }

    @Test
    public void testDoPostDoesNotExist() throws Exception {
        when(accountService.checkUser(username)).thenReturn(false);
        signIn.doPost(request, response);
        verify(request).getParameter("name");
        verify(request).getParameter("password");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(accountService, never()).getUser(username);
        verify(accountService, never()).checkUserlogin(testUser);
        verify(request, never()).setAttribute("user", testUser);
        verify(accountService, never()).addSessions(request.getSession().getId(), testUser);
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("username", username, obj.get("login"));
        assertEquals("password", password, obj.get("password"));
        assertEquals("status", 200, obj.get("status"));
        assertEquals("login_status1", false, obj.get("login_status"));
        assertEquals("username", "User with this name does not exist", obj.get("error_massage"));
    }
    @Test
    public void testDoPostWrongLoginPassword() throws Exception {
        when(accountService.checkUser(username)).thenReturn(true);
        when(accountService.checkUserlogin(testUser)).thenReturn(false);
        when(request.getParameter("password")).thenReturn("wrong_password");
        when(accountService.getUser(username)).thenReturn(testUser);
        signIn.doPost(request, response);
        verify(request).getParameter("name");
        verify(request).getParameter("password");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(request, never()).setAttribute("user", testUser);
        verify(accountService, never()).addSessions(request.getSession().getId(), testUser);
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("username", username, obj.get("login"));
        assertEquals("status", 200, obj.get("status"));
        assertEquals("login_status2", obj.get("login_status"), false);
        assertEquals("username", "Wrong login/password", obj.get("error_massage"));
    }
    @Test
    public void testDoPostSucsess() throws Exception {
        when(accountService.checkUser(username)).thenReturn(true);
        when(accountService.checkUserlogin(testUser)).thenReturn(false);
        when(accountService.getUser(username)).thenReturn(testUser);
        signIn.doPost(request, response);
        verify(request).getParameter("name");
        verify(request).getParameter("password");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(request).setAttribute("user", testUser);
        verify(accountService).addSessions(request.getSession().getId(), testUser);
        JSONObject obj = new JSONObject(stringWriter.toString());
        assertEquals("username", username, obj.get("login"));
        assertEquals("password", password, obj.get("password"));
        assertEquals("status", 200, obj.get("status"));
        assertEquals("login_status3", true, obj.get("login_status"));
        assertEquals("username", "", obj.get("error_massage"));
    }
}
*/