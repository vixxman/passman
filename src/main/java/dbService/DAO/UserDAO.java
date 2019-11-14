package dbService.DAO;

import dbService.models.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class UserDAO {
    private Session session;

    public UserDAO(Session session) {
        this.session =  session;
    }


    public long getUserId(String name) throws HibernateException {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = criteriaBuilder.createQuery(User.class);
        Root<User> usersDataSetRoot = criteria.from(User.class);
        return ((User)criteria.where(criteriaBuilder.equal(usersDataSetRoot.get("name"), name))).getId();
    }

    public User get(String name) throws HibernateException{
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> usersDataSetRoot = criteriaQuery.from(User.class);
        criteriaQuery.select(usersDataSetRoot);
        criteriaQuery.where(criteriaBuilder.equal(usersDataSetRoot.get("name"),name));
        Query<User> q=session.createQuery(criteriaQuery);
        if(q.getResultList().isEmpty()){
            return null;
        }
        else{
            return q.getSingleResult();
        }
    }

    public void insertUser(User user) throws HibernateException {
         session.save(user);
    }
}
