    package servlets;

import dbService.models.User;
import services.AccountService;
import services.EncryptionService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;

    public class SignInServlet extends HttpServlet {

    private AccountService accountService;

    public void SignInServlet(){

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { //авторизация
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
        Timestamp t1=new Timestamp(System.currentTimeMillis());
        if(login==null ||password==null){
            resp.setContentType("text/html:charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        User user = accountService.getUserByLogin(login);
        if(user==null || !user.getPassword().equals(password)){
            resp.setContentType("text/html:charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        else{
            if(accountService.userIsLogged(user.getLogin())){
                resp.setContentType("text/html:charset=utf-8");
                resp.setHeader("A", "userIsLogged");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            else{
                accountService.LogUser(user);
                accountService.updateSession(login, t1);
                resp.setContentType("text/html:charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }

    public void setAccountService(AccountService accountService){
        this.accountService=accountService;
    }

}
