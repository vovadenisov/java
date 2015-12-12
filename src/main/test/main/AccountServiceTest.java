package main;

import database.dbServise.DBService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.AfterClass;
import parser.ConfigParser;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Created by alla on 22.10.15.
 */

public class AccountServiceTest {
    private AccountService accountService;
    private static DBService dbService;
    private UserProfile testUser;
    private String testSession;
    private final String username = "Test";
    private final String password = "test_password";
    private final String email = "test@mail";
    private static ConfigParser configParser = mock(ConfigParser.class);

    @BeforeClass
    public static void bedore() throws Exception {
        when(configParser.getDBName()).thenReturn("testJavaDB");
        when(configParser.getDBUser()).thenReturn("javaTestUser");
        when(configParser.getDBPassword()).thenReturn("123456789java");
        when(configParser.getDBdialect()).thenReturn("org.hibernate.dialect.MySQLDialect");
        when(configParser.getDBdriver()).thenReturn("com.mysql.jdbc.Driver");
        when(configParser.getDBinitialization()).thenReturn("create-drop");
        dbService = new DBService(configParser);

    }

    @Before
    public void initialization() throws Exception {
        testSession = "test_session";
        accountService  = new AccountService(dbService);
        testUser = new UserProfile(username, password, email);
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
       // when(dbService.getCount()).thenReturn(1);
        assertEquals("testNumberOfRegistered().expect 1", 1, accountService.numberOfRegistered());
    }

   @Test
    public void testAddUser() throws Exception {
       UserProfile test1User = new UserProfile("Test1", "test_password1", "test1@mail.ru");
       int size = accountService.numberOfRegistered();
       assertTrue(accountService.addUser(test1User.getLogin(), test1User.getPassword(), test1User.getEmail()));
       assertFalse("testAddUser().False, User already exists!", accountService.addUser(username, password, email));
       assertEquals("testAddUser().user is not added", size + 1, accountService.numberOfRegistered());
       assertEquals("testAddUser().user is not added", test1User.getLogin(), accountService.getUser("Test1").getLogin());
       assertEquals("testAddUser().user is not added", test1User.getPassword(), accountService.getUser("Test1").getPassword());
       assertEquals("testAddUser().user is not added", test1User.getEmail(), accountService.getUser("Test1").getEmail());
       dbService.deleteUser(dbService.readByName("Test1"));
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
     //   when(dbService.readByName(username)).thenReturn(testUser);
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


    @AfterClass
    public static void close(){
        dbService.shutdown();
    }

}
