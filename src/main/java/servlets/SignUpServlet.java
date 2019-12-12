package servlets;

import dbService.dbException;
import dbService.models.User;
import org.hibernate.HibernateException;
import org.quartz.SchedulerException;
import services.AccountService;
import services.EncryptionService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

public class SignUpServlet extends HttpServlet {
    private AccountService accountService;

    public void SignUpServlet(){
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        String login;
        String password;
        try{
            login= EncryptionService.DecryptAES(req.getParameter("login"));
            password=EncryptionService.DecryptAES(req.getParameter("password"));
        }catch (Exception e){
            e.printStackTrace();
            resp.setContentType("text/html:charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }
        if(login==null || password==null){
            resp.setContentType("text/html:charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        User user = accountService.getUserByLogin(login);
        if(user!=null){
            resp.setContentType("text/html:charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_FOUND);
            return;
        }
        if(password.length()<8){
            resp.setContentType("text/html:charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        user = new User(login, password, new Timestamp(System.currentTimeMillis()));
        accountService.AddNewUser(user);
        resp.setContentType("text/html:charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    public void setAccountService(AccountService accountService){
        this.accountService=accountService;
    }
}
