package main;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Created by alla on 02.11.15.
 */
public class RoomServiceTest {
    private RoomService roomService;
    private Room room = mock(Room.class);
    private UserProfile testUser;
    private Team team;
    private final String username = "test_username";
    private final String password = "test_password";
    private final String email = "test_email@mail";
    private final Integer id = 1;

    @Before
    public void initialization() throws Exception {
        testUser = new UserProfile(username, password, email, id);
        team = new Team();
        team.addMembers(testUser);
        roomService = new RoomService();
        room = new Room(team);
    }

    @Test
    public void testPutRoom() throws Exception {
        Integer response_not_added = -1;
        assertNotNull("testPutRoom()", roomService.putRoom(room));
        assertEquals("testPutRoom()", response_not_added, roomService.putRoom(room));
    }

    @Test
    public void testPushEvent() throws Exception{
        assertFalse(roomService.pushEvent("no_push", testUser, id));
    }
    @Test
    public void testPushEvent1() throws Exception{
        Integer id =  roomService.putRoom(room);
        assertTrue(roomService.pushEvent("push", testUser, id));
    }

    @Test
    public void testGetRoom() throws Exception{
        assertNotNull(roomService.getRoom(roomService.putRoom(room)));
        assertNull(roomService.getRoom(id));
    }
    @Test
    public void testUserInRoom() throws Exception{
        assertFalse(roomService.userInRoom(testUser));
        roomService.putRoom(room);
        assertTrue(roomService.userInRoom(testUser));
    }
    @Test
    public void testGetRoomWithUser() throws Exception{
        Integer response = -1;
        assertEquals(response, roomService.getRoomWithUser(testUser));
        assertEquals(roomService.putRoom(room), roomService.getRoomWithUser(testUser));
    }
}