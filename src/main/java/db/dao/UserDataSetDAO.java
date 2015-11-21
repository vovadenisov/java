package db.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
//import db.dataSets.UserDataSet;
import main.UserProfile;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by alla on 21.11.15.
 */
public class UserDataSetDAO {

    private Session session;

    public UserDataSetDAO(Session session) {
        this.session = session;
    }

    public void save(UserProfile dataSet) throws SQLException{
        session.save(dataSet);
        session.close();
    }

    public UserProfile read(long id) throws SQLException{
        return (UserProfile)session.load(UserProfile.class, id);
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
