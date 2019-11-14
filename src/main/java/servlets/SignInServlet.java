package servlets;

import dbService.models.User;
import services.AccountService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignInServlet extends HttpServlet {

    private AccountService accountService;

    public void SignInServlet(){

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { //авторизация
        String login=req.getParameter("login");
        String password=req.getParameter("password");
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
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            else{
                accountService.setCodeForUser(login);
                accountService.setJob(login);
                accountService.LogUser(user);
                resp.setContentType("text/html:charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }

    public void setAccountService(AccountService accountService){
        this.accountService=accountService;
    }

}
