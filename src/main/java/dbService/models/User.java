package dbService.models;

import javax.persistence.*;
import java.io.Serializable;
import com.google.gson.Gson;

@Entity
@Table(name="users")
public class User implements Serializable{

    private static final long serialVersionUID = -6887265719829270424L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", unique = true, updatable = false)
    private String name;

    @Column(name="password")
    private String password;

    @Column(name="request")
    private boolean request;

    /*@Column (name="logged")
    private boolean logged;

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }*/

    public boolean isRequest() {
        return request;
    }

    public void setRequest(boolean request) {
        this.request = request;
    }

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
    public User(String name, String password){
        this.setLogin(name);
        this.setPassword(password);
        this.request=false;
        //this.logged=true;
    }

    @SuppressWarnings("UnusedDeclaration")
    public User(String name, String password, boolean req){
        this.setLogin(name);
        this.setPassword(password);
        this.request=req;
        //this.logged=true;
    }

    public String toJson() {
        Gson gson=new Gson();
        return gson.toJson(this);
    }

}
