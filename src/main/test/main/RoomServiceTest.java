package main;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;

import javax.servlet.http.HttpSession;


/**
 * Created by alla on 02.11.15.
 */
public class RoomServiceTest {
    private RoomService roomService;
    private Room room = mock(Room.class);
    private UserProfile testUser;
    private final String username = "test_username";
    private final String password = "test_password";
    private final String email = "test_email@mail";
    private final Integer id = 1;

    @Before
    public void initialization() throws Exception {
        roomService = new RoomService();
        testUser = new UserProfile(username, password, email, id);
    }

    @Test
    public void testPutRoom() throws Exception {
        Integer response_not_added = -1;
        assertNotNull("testPutRoom()", roomService.putRoom(room));
        assertEquals("testPutRoom()", response_not_added, roomService.putRoom(room));
    }
}