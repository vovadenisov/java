package main;

import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
/**
 * Created by alla on 02.11.15.
 */
public class TeamTest {
    private Team team;
    private final Integer minSize = 2;
    UserProfile testUser;
    private final String username = "Test";
    private final String password = "test_password";
    private final String email = "test@mail";
    private final Integer id = 1;

    @Before
    public void initialization() throws Exception {
        team = new Team();
        testUser = new UserProfile(username, password, email, id);
        team.setTeamSize(2);
    }
    @Test
    public void testSetTeamSize() throws Exception {
        Integer newTeamSize = 3;
        team.getTeamSize();
        assertEquals("testSetTeamSize(). Expect 2", minSize, team.getTeamSize());
        team.setTeamSize(newTeamSize);
        assertEquals("testSetTeamSize(). Expect 3", newTeamSize, team.getTeamSize());
    }

    @Test
    public void testGetTeamSize() throws Exception {
        Integer newTeamSize = 3;
        team.getTeamSize();
        assertEquals("testSetTeamSize(). Expect 2", minSize, team.getTeamSize());
        team.setTeamSize(newTeamSize);
        assertEquals("testSetTeamSize(). Expect 3", newTeamSize, team.getTeamSize());
    }
    @Test
    public void testAddMembers() throws Exception {
        assertTrue("testAddMembers(). Expect true", team.addMembers(testUser));
        UserProfile neeUser1 = new UserProfile("new_name", "new_pass", "new_email", 1);
        assertTrue("testAddMembers(). Expect true", team.addMembers(neeUser1));
        UserProfile neeUser2 = new UserProfile("new_name", "new_pass", "new_email", 1);
        assertFalse("testAddMembers(). Expect false", team.addMembers(neeUser2));
    }

    @Test
    public void testGetMembers() throws Exception {
        assertEquals("testGetMembers", 0, team.getMembers().size() );
        team.addMembers(testUser);
        assertEquals("testGetMembers", 1, team.getMembers().size());
    }
}