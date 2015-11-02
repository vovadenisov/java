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
    private final Team team = mock(Team.class);

    @Before
    public void initialization() throws Exception {
        score = new Score(team);
    }
    @Test
    public void testIsTeam() throws Exception {
        assertTrue("testIsTeam(). Expect true", score.isTeam(team));
        Team newTeam = new Team();
        assertFalse("testIsTeam(). Expect false", score.isTeam(newTeam));

    }
}