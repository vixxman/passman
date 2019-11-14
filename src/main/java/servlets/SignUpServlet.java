package servlets;

import dbService.dbException;
import dbService.models.User;
import org.hibernate.HibernateException;
import org.quartz.SchedulerException;
import services.AccountService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SignUpServlet extends HttpServlet {
    private AccountService accountService;

    public void SignUpServlet(){
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        String login=req.getParameter("login");
        String password=req.getParameter("password");
        if(login==null || password==null){
            resp.setContentType("text/html:charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        User user = accountService.getUserByLogin(login);
        if(user!=null){
            resp.setContentType("text/html:charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        user = new User(login, password);
        accountService.AddNewUser(user);
        resp.setContentType("text/html:charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    public void setAccountService(AccountService accountService){
        this.accountService=accountService;
    }
}
