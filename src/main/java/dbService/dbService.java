package dbService;

import dbService.DAO.UserDAO;
import dbService.models.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;


public class dbService {
    private static final String hibernate_show_sql =  " true";
    private static final String hibernate_hbm2ddl_auto =  " create";

    private final SessionFactory sessionFactory;

    public dbService(){
        Configuration conf=getMySqlConfiguration();
        sessionFactory=createSessionFactory(conf);
    }

    @SuppressWarnings("UnusedDeclaration")
    private Configuration getMySqlConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(User.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        //configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/serverdb?reconnect=true&serverTimezone=UTC&useLegacyDatetimeCode=false");
        //configuration.setProperty("hibernate.connection.url", "jdbc:mysql://eu-cdbr-west-02.cleardb.net/heroku_0e42ab44acebf93?reconnect=true&createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false");
        //configuration.setProperty("hibernate.connection.username", "b19ae7cb061813");
        //configuration.setProperty("hibernate.connection.password", "37b73ebf");
        configuration.setProperty("hibernate.connection.username", "root");
        configuration.setProperty("hibernate.connection.password", "9379992Gg");
        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);
        configuration.setProperty("hibernate.c3p0.min_size","5");
        configuration.setProperty("hibernate.c3p0.max_size","20");
        configuration.setProperty("hibernate.c3p0.timeout","300");
        configuration.setProperty("hibernate.c3p0.max_statements","50");
        return configuration;
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    public void addUser(User user){
        Session session=sessionFactory.openSession();
        Transaction tx=session.beginTransaction();
        UserDAO userDAO=new UserDAO(session);
        userDAO.insertUser(user);
        tx.commit();
        session.close();
    }

    public User getUser(String name) {
        Session session = sessionFactory.openSession();
        UserDAO dao = new UserDAO(session);
        User dataSet = dao.get(name);
        session.close();
        return dataSet;
    }

    public void updateUserSession(String name, Timestamp time){
        Session session = sessionFactory.openSession();
        UserDAO dao = new UserDAO(session);
        User dataSet = dao.get(name);
        dataSet.setSession(time);
        Transaction tx=session.beginTransaction();
        tx.commit();
        session.close();
    }

    public void deleteUser(String name)throws dbException{
        try{
            Session session=sessionFactory.openSession();
            UserDAO dao = new UserDAO(session);
            User dataSet = dao.get(name);
            Transaction tx=session.beginTransaction();
            session.delete(dataSet);
            tx.commit();
            session.close();
        }
        catch (HibernateException ex){
            throw new dbException(ex);
        }
    }



}

