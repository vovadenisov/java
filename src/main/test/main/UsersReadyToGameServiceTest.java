package main;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by alla on 02.11.15.
 */
public class UsersReadyToGameServiceTest {
    private UsersReadyToGameService usersReadyToGameService;
    private UserProfile testUser;
    private final String username = "test_username";
    private final String password = "test_password";
    private final String email = "test_email@mail";
    private final Integer id = 1;
    @Before
    public void initialization() throws Exception {
        usersReadyToGameService = new UsersReadyToGameService();
        testUser = new UserProfile(username, password, email);
    }

    @Test
    public void testPopUserReady() throws Exception {
        usersReadyToGameService.addUserToReady(testUser);
        assertEquals("testGetUserReady().Expect 1", 1, usersReadyToGameService.getUserReady().size());
        usersReadyToGameService.popUserReady(testUser);
        assertEquals("testGetUserReady().Expect 0", 0, usersReadyToGameService.getUserReady().size());
    }

    @Test
    public void testAddUserToReady() throws Exception {
        usersReadyToGameService.addUserToReady(testUser);
        assertTrue("testAddUserToReady(). User successfully added", usersReadyToGameService.checkUser(testUser.getLogin()));
    }
    @Test
    public void testGetUserReady() throws Exception {
        assertEquals("testGetUserReady().Expect 0", 0, usersReadyToGameService.getUserReady().size());
        usersReadyToGameService.addUserToReady(testUser);
        assertEquals("testGetUserReady().Expect 1", 1, usersReadyToGameService.getUserReady().size());
    }
    @Test
    public void testCheckUser() throws Exception {
        assertFalse("testCheckUser(). User does not exist",usersReadyToGameService.checkUser(username));
        usersReadyToGameService.addUserToReady(testUser);
        assertTrue("testCheckUser(). User exist", usersReadyToGameService.checkUser(testUser.getLogin()));
    }
}