package servlets;

import dbService.models.User;
import services.AccountService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CodeServlet extends HttpServlet {

    private  AccountService accountService;

    public void CodeServlet(){
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String code = req.getParameter("code");

        boolean b=accountService.userIsLogged(login);
        boolean m=accountService.userIsLoggedM(login);
        if(b==false ||m ==false){
            resp.setContentType("text/html:charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else{
            if(code.equals(accountService.getCodeForUser(login))){
                resp.setContentType("text/html:charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_OK);
            }
            else{
                resp.setContentType("text/html:charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    public void setAccountService(AccountService accountService){
        this.accountService=accountService;
    }
}
