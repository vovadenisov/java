package main;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Created by alla on 05.11.15.
 */
public class RoomTest {
    private Room room;
    private UserProfile testUser;
    private Team team;
    private Score score;
    private final String username = "Test";
    private final String password = "test_password";
    private final String email = "test@mail";
    private final Integer id = 1;

    @Before
    public void initialization() throws Exception {
        testUser = new UserProfile(username, password, email, id);
        team = new Team();
        team.addMembers(testUser);
        room = new Room(team);
        room.addTeam(team);
        score = new Score(team);
    }

    @Test
    public void testAddEvent() throws Exception {
        String event = "no_push";
        assertFalse(room.addEvent(event, testUser));
        event = "push";
        assertTrue(room.addEvent(event, testUser));
        assertEquals(testUser,room.getWiner().getMembers().toArray()[0] );
    }

    @Test
    public void testIncrimentScore() throws Exception {
        room.incrimentScore(team);

    }

    @Test
    public void testGetTeamUser() throws Exception {
        assertNotNull(room.getTeamUser(testUser));
        UserProfile testUser2 = new UserProfile("username", "pass", "email", 1);
        Integer response = -1;
        assertEquals(response, room.getTeamUser(testUser2));

    }
    @Test
    public void testAddTeamFalse() throws Exception {
        Team team2 = new Team();
        team2.addMembers(testUser);
        assertFalse(room.addTeam(team2));
    }
    @Test
    public void testAddTeamTrue() throws Exception {
        Team team2 = new Team();
        UserProfile testUser2 = new UserProfile("username", "pass", "email", 1);
        team2.addMembers(testUser2);
        assertTrue(room.addTeam(team2));
    }

    @Test
    public void testGetTeams() throws Exception {

    }

    @Test
    public void testGetUsers() throws Exception {

    }

    @Test
    public void testGetMyTeamUsers() throws Exception {

    }
}