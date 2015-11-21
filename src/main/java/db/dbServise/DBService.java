package db.dbServise;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
//import db.dataSets.UserDataSet;
import main.UserProfile;
import db.dao.UserDataSetDAO;
import java.sql.SQLException;

/**
 * Created by alla on 21.11.15.
 */

public class DBService {
    /*
        Фабрика, которая создает сессии
     */
    private SessionFactory sessionFactory;


    public DBService(){
        Configuration configuration = new Configuration();

        /*
            Нужно в конфиг положить все датасеты
         */
        configuration.addAnnotatedClass(UserProfile.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/javaDB");
        configuration.setProperty("hibernate.connection.username", "javaTestUser");
        configuration.setProperty("hibernate.connection.password", "123456789java");
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

    public void saveUser(UserProfile dataSet){
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            UserDataSetDAO dao = new UserDataSetDAO(session);
            dao.save(dataSet);
            transaction.commit();
        }catch (Exception e)
        {
            System.out.println("ERROR "+e.toString());
        }

    }

    public UserProfile read(long id) {
        try {
            Session session = sessionFactory.openSession();
            UserDataSetDAO dao = new UserDataSetDAO(session);
            return dao.read(id);
        }
       catch (Exception e){
           System.out.println("ERROR "+e.toString());
           return null;
       }
    }

    public UserProfile readByName(String name) {
        try {
            Session session = sessionFactory.openSession();
            UserDataSetDAO dao = new UserDataSetDAO(session);
            return dao.readByName(name);
        }
        catch (Exception e){
            System.out.println("ERROR "+e.toString());
            return null;
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
