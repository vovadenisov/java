package main;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by alla on 22.10.15.
 */

public class AccountServiceTest {
    private AccountService accountService;
    private UserProfile testUser;
    private String testSession;
    private final String username = "Test";
    private final String password = "test_password";
    private final String email = "test@mail";
    private final Integer id = 1;

    @Before
    public void initialization() throws Exception {
        testSession = "test_session";
        accountService  = new AccountService();
        testUser = new UserProfile(username, password, email, id);
        accountService.addUser(username, password, email);
    }

    @Test
    public void testCheckUser() throws Exception {
        assertTrue("testCheckUser().Expect true", accountService.checkUser(username));
        assertFalse("testCheckUser().Expect false", accountService.checkUser("Test1"));
    }

    @Test
    public void testCheckUserlogin() throws Exception {
        assertNotNull("testCheckUserlogin. Expect true", accountService.checkUserlogin(testUser));
    }


    @Test
    public void testNumberOfSessions() throws Exception {
        assertEquals("testNumberOfSessions().expect 0", 0, accountService.numberOfSessions());
        accountService.addSessions(testSession, testUser);
        assertEquals("testNumberOfSessions().expect 1", 1, accountService.numberOfSessions());
        accountService.removeSeassions(testSession);
        assertEquals("testNumberOfSessions().expect 0", 0, accountService.numberOfSessions());
    }

    @Test
    public void testNumberOfRegistered() throws Exception {
        assertEquals("testNumberOfRegistered().expect 1", 1, accountService.numberOfRegistered());
        String[] test_user_data = new String[]{"Test1", "test_password1", "test1@mail.ru"};
        accountService.addUser(test_user_data[0], test_user_data[1], test_user_data[2]);
        assertEquals("testNumberOfRegistered().expect 2", 2, accountService.numberOfRegistered());
    }

    @Test
    public void testAddUser() throws Exception {
        String[] test_user_data = new String[]{"Test1", "test_password1", "test1@mail.ru"};
        int size = accountService.numberOfRegistered();
        assertTrue("testAddUser().User hasn't added!", accountService.addUser(test_user_data[0], test_user_data[1], test_user_data[2]));
        assertFalse("testAddUser().False, User already exists!", accountService.addUser(username, password, email));
        assertEquals("testAddUser().user is not added", size + 1, accountService.numberOfRegistered());
    }

    @Test
    public void testRemoveSeassions() throws Exception {
        assertFalse("testRemoveSeassions().testRemoveSeassions", accountService.removeSeassions(testSession));
        accountService.addSessions(testSession, testUser);
        assertEquals("testRemoveSeassions().testRemoveSeassions", 1, accountService.numberOfSessions());
        assertTrue("testRemoveSeassions().testRemoveSeassions", accountService.removeSeassions(testSession));
        assertEquals("testRemoveSeassions().testRemoveSeassions", 0, accountService.numberOfSessions());
    }
    @Test
    public void testGetUser() throws Exception {
        assertNotNull("testGetUser().function does not give an existing object", accountService.getUser(username));
        assertNull("testGetUser().function gives a non-existent object", accountService.getUser("Test1"));
        assertEquals("testGetUser().not a valid response", testUser.getLogin(), accountService.getUser(username).getLogin());
        assertEquals("testGetUser().not a valid response", testUser.getEmail(), accountService.getUser(username).getEmail());
        assertEquals("testGetUser().not a valid response", testUser.getPassword(), accountService.getUser(username).getPassword());
    }

    @Test
    public void testGetSessinonId() throws Exception {
        String  expectSessionId = "";
        assertEquals(expectSessionId, accountService.getSessinonId(testUser));
        accountService.addSessions(testSession, testUser);
        assertEquals(testSession, accountService.getSessinonId(testUser));
    }
    @Test
    public void testCheckSeassions() throws Exception {
        assertFalse(accountService.checkSeassions(testSession));
        accountService.addSessions(testSession, testUser);
        assertTrue(accountService.checkSeassions(testSession));
    }
    @Test
    public void testAddSessions() throws Exception {
        assertFalse(accountService.checkSeassions(testSession));
        accountService.addSessions(testSession, testUser);
        assertTrue(accountService.checkSeassions(testSession));
    }
    @Test
    public void testGetCurrentUser() throws Exception {
        accountService.addSessions(testSession, testUser);
        assertEquals(testUser, accountService.getCurrentUser(testSession));
    }
    @Test
    public void testUserSession() throws Exception {
        String  expectLogin = "";
        assertEquals(expectLogin ,accountService.userSession(testSession));
        accountService.addSessions(testSession, testUser);
        assertEquals(testUser.getLogin(), accountService.userSession(testSession));
    }
    @Test
    public void testGetSessions() throws Exception {
        accountService.addSessions(testSession, testUser);
        assertEquals(testUser, accountService.getSessions(testSession));
    }
}
