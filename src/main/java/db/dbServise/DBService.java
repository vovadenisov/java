package db.dbServise;

import db.dao.UserProfileDAO;
import main.UserProfile;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by alla on 21.11.15.
 */

public class DBService {
    /*
        Фабрика, которая создает сессии
     */
    private SessionFactory sessionFactory;

    public DBService(String dbUser, String dbPassword, String dbName){
        Configuration configuration = new Configuration();

        /*
            Нужно в конфиг положить все датасеты
         */
        configuration.addAnnotatedClass(UserProfile.class);


        String fullDBName = "jdbc:mysql://localhost:3306/" + dbName;
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", fullDBName);
        configuration.setProperty("hibernate.connection.username", dbUser);
        configuration.setProperty("hibernate.connection.password", dbPassword);
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");

        sessionFactory = createSessionFactory(configuration);

    }


    public String getLocalStatus() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        String status = transaction.getStatus().toString();
        session.close();
        return status;
    }

    public Integer getCount(){
        Integer count = readAll().size();
        return count;
    }

    public List<UserProfile> readAll() {
        Session session = sessionFactory.openSession();
        UserProfileDAO dao = new UserProfileDAO(session);
        return dao.readAll();
    }

    public void saveUser(String name, String password, String email) throws SQLException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            UserProfileDAO dao = new UserProfileDAO(session);
            UserProfile dataSet = new UserProfile(name, password, email);
            dao.save(dataSet);
            transaction.commit();
            System.out.print("exit from save");
        }catch (SQLException e)
        {
            System.out.println("error when you try to save a user named " + name);
            throw e;
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
       catch (Exception e){
           System.out.println("error when trying to read data about the user id "+ id);
           return null;
       }
    }

    public UserProfile readByName(String name) {
        Session session = sessionFactory.openSession();
        UserProfileDAO dao = new UserProfileDAO(session);
        UserProfile userProfile = null;
        try {
            userProfile = dao.readByName(name);
        }
        catch (SQLException e){
            System.out.println("error when trying to read data about the user named "+ name);
        }finally {
            session.close();
            return userProfile;
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
