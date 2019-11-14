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
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/db_example?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false");
        configuration.setProperty("hibernate.connection.username", "root");
        configuration.setProperty("hibernate.connection.password", "root");
        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);
        return configuration;
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    public long addUser(User user){
        Session session=sessionFactory.openSession();
        Transaction tx=session.beginTransaction();
        UserDAO userDAO=new UserDAO(session);
        long id=userDAO.insertUser(user);
        tx.commit();
        session.close();
        return id;
    }

    public User getUser(String name) {
        Session session = sessionFactory.openSession();
        UserDAO dao = new UserDAO(session);
        User dataSet = dao.get(name);
        session.close();
        return dataSet;
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

