package db.dao;

import main.UserProfile;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.sql.SQLException;

//import db.dataSets.UserDataSet;
/**
 * Created by alla on 21.11.15.
 */
public class UserProfileDAO {

    private Session session;

    public UserProfileDAO(Session session) {
        this.session = session;
    }

    public void save(UserProfile dataSet) throws SQLException{
        session.save(dataSet);
        session.close();
    }

    public UserProfile read(long id) throws SQLException{
        return (UserProfile) session.load(UserProfile.class, id);
    }

    public UserProfile readByName(String login) throws SQLException{
        Criteria criteria = session.createCriteria(UserProfile.class);
        return (UserProfile) criteria.add(Restrictions.eq("login", login)).uniqueResult();
    }

    public void delete(UserProfile dataSet) throws SQLException{
        session.delete(dataSet);
        session.close();
    }
}
