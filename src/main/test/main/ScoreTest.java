package main;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
/**
 * Created by alla on 02.11.15.
 */
public class ScoreTest {
    private Score score;
    private Team team;
    private final String username = "Test";
    private final String password = "test_password";
    private final String email = "test@mail";
    private final Integer id = 1;
    private UserProfile testUser;
    @Before
    public void initialization() throws Exception {
        testUser = new UserProfile(username, password, email, id);
        team = new Team();
        score = new Score(team);
    }
    @Test
    public void testIsTeam() throws Exception {
        assertTrue("testIsTeam(). Expect true", score.isTeam(team));
        Team newTeam = new Team();
        assertFalse("testIsTeam(). Expect false", score.isTeam(newTeam));

    }
    @Test
    public void testIncrementScore() throws Exception {
        Integer first_score = score.getScore();
        score.incrementScore();
        assertEquals((Integer) (first_score + 1), score.getScore());
    }

    @Test
    public void testGetScore() throws Exception {
        Integer response = 0;
        assertEquals(response, score.getScore());
        score.incrementScore();
        assertEquals((Integer)(response + 1), score.getScore());
    }

    @Test
    public void testGetTeam() throws Exception {
        assertEquals(team, score.getTeam());
        team.addMembers(testUser);
        assertEquals(team, score.getTeam());
    }
}