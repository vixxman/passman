package dbService.models;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

import com.google.gson.Gson;

@Entity
@Table(name="users")
public class User implements Serializable{

    private static final long serialVersionUID = -6887265719829270424L;

    @Id
    @Column(name = "id", unique=true, nullable=false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name", unique = true, updatable = false)
    private String name;

    @Column(name="password")
    private String password;

    @Column(name="Timestamp", nullable = false)
    private Timestamp session;


    @SuppressWarnings("UnusedDeclaration")
    public User(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return name;
    }

    public void setLogin(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @SuppressWarnings("UnusedDeclaration")
    public User(String name, String password, Timestamp time){
        this.setLogin(name);
        this.setPassword(password);
        this.setSession(time);
    }

    public String toJson() {
        Gson gson=new Gson();
        return gson.toJson(this);
    }

    public Timestamp getSession(){
        return session;
    }

    public void setSession(Timestamp session){
        this.session = session;
    }

}
