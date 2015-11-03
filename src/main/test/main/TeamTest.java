package main;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
/**
 * Created by alla on 02.11.15.
 */
public class TeamTest {
    private Team team;
    private final Integer minSize = 1;
    UserProfile testUser;
    private final String username = "Test";
    private final String password = "test_password";
    private final String email = "test@mail";
    private final Integer id = 1;

    @Before
    public void initialization() throws Exception {
        team = new Team();
        testUser = new UserProfile(username, password, email, id);
    }

    /*
    public void setTeamSize(Integer size){
        teamSize = size;
    }

    public Integer getTeamSize(){
        return teamSize;
    }

    public Boolean addMembers(UserProfile member){
        if (members.size() < teamSize && !members.contains(member)) {
            members.add(member);
            return  true;
        }
        return false;
    }

    public Set<UserProfile> getMembers(){
        return members;
    }
    */



    @Test
    public void testSetTeamSize() throws Exception {
        Integer newTeamSize = 3;
        assertEquals("testSetTeamSize().before_test Expect 1", minSize, team.getTeamSize());
        team.setTeamSize(newTeamSize);
        assertEquals("testSetTeamSize().after_set_3 Expect 3", newTeamSize, team.getTeamSize());
        Integer correct_size = team.getTeamSize();
        newTeamSize = -1;
        team.setTeamSize(newTeamSize);
        assertEquals("testSetTeamSize().set_-1 Expect 3", correct_size, team.getTeamSize());
    }



    @Test
    public void testGetTeamSize() throws Exception {
        assertEquals("testGetTeamSize(). Expect 1", team.getTeamSize(), team.getTeamSize());
        Integer newTeamSize = 3;
        team.setTeamSize(newTeamSize);
        assertEquals("testGetTeamSize(). Expect 3", newTeamSize, team.getTeamSize());
    }
    @Test
    public void testAddMembers() throws Exception {
        assertTrue("testAddMembers(). Expect true", team.addMembers(testUser));
//        в комнату не долбавится больше юзеров, чем размер комнаты
        UserProfile neeUser1 = new UserProfile("new_name", "new_pass", "new_email", 2);
        team.setTeamSize(1);
        assertFalse("testAddMembers().add_new_member_size_1 Expect false", team.addMembers(neeUser1));
//        в комнау с размером 2 пытаемся положить уже лежащего там юзера
        team.setTeamSize(2);
        assertFalse("testAddMembers().add_testUser_again Expect false", team.addMembers(testUser));
//      пытаемся положить 2 юзера в комнату, где должно быть 2 юзера
        assertTrue("testAddMembers(). Expect false", team.addMembers(neeUser1));
    }

    @Test
    public void testGetMembers() throws Exception {
        //проверяем, что вернет пустой сет если не добавляли
        assertEquals("testGetMembers", 0, team.getMembers().size());
        //добавим тестового юзера и проверим
        Set<UserProfile> users = new HashSet<>();
        users.add(testUser);
        team.addMembers(testUser);
        assertArrayEquals(team.getMembers().toArray(), users.toArray());
        //проверим, что не добавился лишний юзер, который уже был
        team.addMembers(testUser);
        assertArrayEquals(team.getMembers().toArray(), users.toArray());
        //проверим, что новый добавленный юзер возвращается
        UserProfile neeUser1 = new UserProfile("new_name", "new_pass", "new_email", 2);
        team.setTeamSize(2);
        team.addMembers(neeUser1);
        users.add(neeUser1);
        assertArrayEquals(team.getMembers().toArray(), users.toArray());
    }
}