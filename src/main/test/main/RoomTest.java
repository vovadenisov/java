package main;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by alla on 05.11.15.
 */
public class RoomTest {
    private Room room;
    private UserProfile testUser;
    private Team team;
    private Team team2;
    private UserProfile testUser2;
    private Score score;
    private final String username = "Test";
    private final String password = "test_password";
    private final String email = "test@mail";
    private final Integer id = 1;

    @Before
    public void initialization() throws Exception {
        testUser = new UserProfile(username, password, email);
        team = new Team();
        team.addMembers(testUser);
        room = new Room(team);
        room.addTeam(team);
        score = new Score(team);
        team2 = new Team();
        testUser2 = new UserProfile("username", "pass", "email");
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
        UserProfile testUser2 = new UserProfile("username", "pass", "email");
        Integer response = -1;
        assertEquals(response, room.getTeamUser(testUser2));

    }
    @Test
    public void testAddTeamFalse() throws Exception {
        team2.addMembers(testUser);
        assertFalse(room.addTeam(team2));
    }
    @Test
    public void testAddTeamTrue() throws Exception {
        team2.addMembers(testUser2);
        assertTrue(room.addTeam(team2));
    }

    @Test
    public void testGetTeams() throws Exception {
        Set<Team> expectedSet = new HashSet<Team>();
        expectedSet.add(team);
        int expectedSize = expectedSet.size();
        assertEquals(expectedSize, room.getTeams().size());
        assertArrayEquals(expectedSet.toArray(), room.getTeams().toArray());
        team2.addMembers(testUser2);
        room.addTeam(team2);
        expectedSet.add(team2);
        expectedSize = expectedSet.size();
        assertEquals(expectedSize, room.getTeams().size());
        assertArrayEquals(expectedSet.toArray(), room.getTeams().toArray());
    }

    @Test
    public void testGetUsers() throws Exception {
        Set<UserProfile> expectedUsers = new HashSet<>();
        expectedUsers.add(testUser);
        assertArrayEquals(expectedUsers.toArray(), room.getUsers().toArray());
        team2.addMembers(testUser2);
        room.addTeam(team2);
        expectedUsers.add(testUser2);
        assertArrayEquals(expectedUsers.toArray(), room.getUsers().toArray());
    }

    @Test
    public void testGetMyTeamUsers() throws Exception {
        Set<UserProfile> expectedSet = new HashSet<>();
        expectedSet.add(testUser);
        team2.addMembers(testUser2);
        room.addTeam(team2);
        assertArrayEquals(expectedSet.toArray(), room.getMyTeamUsers(testUser).toArray());
        expectedSet.remove(testUser);
        expectedSet.add(testUser2);
        assertArrayEquals(expectedSet.toArray(), room.getMyTeamUsers(testUser2).toArray());
    }

    @Test
    public void testGetEnemyTeamUsers() throws Exception {
        Set<UserProfile> expectedSet = new HashSet<>();
        expectedSet.add(testUser);
        team2.addMembers(testUser2);
        room.addTeam(team2);
        assertArrayEquals(expectedSet.toArray(), room.getEnemyTeamUsers(testUser2).toArray());
        expectedSet.remove(testUser);
        expectedSet.add(testUser2);
        assertArrayEquals(expectedSet.toArray(), room.getEnemyTeamUsers(testUser).toArray());
    }
}