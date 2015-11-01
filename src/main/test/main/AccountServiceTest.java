package main;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import static org.junit.Assert.*;
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
/*
    @Test
    public void testAddUser() throws Exception {
        String[] test_user_data = new String[]{"Test1", "test_password1", "test1@mail.ru"};
        UserProfile user = new UserProfile(test_user_data[0], test_user_data[1], test_user_data[2]);
        int size = accountService.numberOfRegistered();
        assertTrue("testAddUser().User hasn't added!", accountService.addUser(user.getLogin(), user));
        assertFalse("testAddUser().False, User already exists!", accountService.addUser(user.getLogin(), user));
        assertEquals("testAddUser().User hasn't added!", user, accountService.getUser(test_user_data[0]));
        assertEquals("testAddUser().user is not added", size + 1, accountService.numberOfRegistered());
    }
*/
    @Test
    public void testGetUser() throws Exception {
        assertNotNull("testGetUser().function does not give an existing object", accountService.getUser(username));
        assertNull("testGetUser().function gives a non-existent object", accountService.getUser("Test1"));
        assertEquals("testGetUser().not a valid response", testUser, accountService.getUser(username));
    }
 /*   @Test
    public void testNumberOfRegistered() throws Exception {
        assertEquals("testNumberOfRegistered().expect 1", 1, accountService.numberOfRegistered());
        String[] test_user_data = new String[]{"Test1", "test_password1", "test1@mail.ru"};
        accountService.addUser(test_user_data[0], new UserProfile(test_user_data[0], test_user_data[1], test_user_data[2],1 ));
        assertEquals("testNumberOfRegistered().expect 2", 2, accountService.numberOfRegistered());
    }*/
    @Test
    public void testRemoveSeassions() throws Exception {
        assertFalse("testRemoveSeassions().testRemoveSeassions", accountService.removeSeassions(testSession));
        accountService.addSessions(testSession, testUser);
        assertEquals("testRemoveSeassions().testRemoveSeassions", 1, accountService.numberOfSessions());
        assertTrue("testRemoveSeassions().testRemoveSeassions", accountService.removeSeassions(testSession));
        assertEquals("testRemoveSeassions().testRemoveSeassions", 0, accountService.numberOfSessions());
    }
}
