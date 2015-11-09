package main;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;


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

    @Test
    public void testPushEvent() throws Exception{

    }
}