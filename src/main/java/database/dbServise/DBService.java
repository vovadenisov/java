package database.dbServise;

import database.dao.UserProfileDAO;
import exceptions.ConfigException;
import main.UserProfile;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.sql.SQLException;
import java.util.List;
import exceptions.DBException;
import parser.ConfigParser;

/**
 * Created by alla on 21.11.15.
 */

public class DBService {
    private SessionFactory sessionFactory;

    public DBService(ConfigParser configParser) {

        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UserProfile.class);

        try {
            configParser.loadBDConfig();
            String fullDBName = "jdbc:mysql://localhost:3306/" + configParser.getDBName();
            configuration.setProperty("hibernate.dialect", configParser.getDBdialect());
            configuration.setProperty("hibernate.connection.driver_class", configParser.getDBdriver());
            configuration.setProperty("hibernate.connection.url", fullDBName);
            configuration.setProperty("hibernate.connection.username", configParser.getDBUser());
            configuration.setProperty("hibernate.connection.password", configParser.getDBPassword());
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.hbm2ddl.auto", configParser.getDBinitialization());
            sessionFactory = createSessionFactory(configuration);

        }catch (HibernateException | ConfigException e ){
            System.out.println("Failed to connect to database");
        }

    }

    public String getLocalStatus() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String status = transaction.getStatus().toString();
        session.close();
        return status;
    }

    public Integer getCount(){
        return readAll().size();
    }

    public List<UserProfile> readAll() {
        Session session = sessionFactory.openSession();
        UserProfileDAO dao = new UserProfileDAO(session);
        return dao.readAll();
    }

    public void saveUser(String name, String password, String email) throws DBException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            UserProfileDAO dao = new UserProfileDAO(session);
            UserProfile dataSet = new UserProfile(name, password, email);
            dao.save(dataSet);
            transaction.commit();
        }catch (SQLException | HibernateException e)
        {
            throw new DBException("Error when you try to save a user named " + name);
        }finally {
            session.close();
        }
    }

    public void deleteUser(UserProfile userProfile){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {

            UserProfileDAO dao = new UserProfileDAO(session);
            dao.delete(userProfile);
            transaction.commit();
            System.out.println("delete" + userProfile.getLogin());
        }catch (SQLException | HibernateException e)
        {
            System.out.println("error when trying to delete user"+ userProfile.getLogin());
        }finally {
            session.close();
        }
    }

    public UserProfile read(long id) {
        try {
            Session session = sessionFactory.openSession();
            UserProfileDAO dao = new UserProfileDAO(session);
            return dao.read(id);
        }
       catch (HibernateException | SQLException e){
           System.out.println("error when trying to read data about the user id "+ id);
           return null;
       }
    }

    public UserProfile readByName(String name) throws DBException{
        try {
            UserProfile userProfile = null;
            Session session = sessionFactory.openSession();
            UserProfileDAO dao = new UserProfileDAO(session);
            userProfile = dao.readByName(name);
            session.close();
            return userProfile;
        }
        catch (SQLException | NullPointerException e){
            throw new DBException("error when trying to read data about the user named "+ name);
        }
    }

    public void shutdown(){

        sessionFactory.close();
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

}
