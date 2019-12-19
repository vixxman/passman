package servlets;

import dbService.models.User;
import services.AccountService;
import services.EncryptionService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

public class CodeServlet extends HttpServlet {

    private  AccountService accountService;

    public void CodeServlet(){
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login;
        String code;

        try{
            login= EncryptionService.DecryptAES(req.getParameter("login"));
            code = EncryptionService.DecryptAES(req.getParameter("code"));
        }catch (Exception e){
            e.printStackTrace();
            resp.setContentType("text/html:charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }

        Timestamp t1= new Timestamp(System.currentTimeMillis());
        //boolean b=accountService.userIsLogged(login);
        boolean m=accountService.userIsLoggedM(login);
        if(m ==false){
            resp.setContentType("text/html:charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else{
            User u=accountService.getUserByLogin(login);
            Timestamp t2 =accountService.getTimestamp(login);
            long a=t2.getTime();
            long a2=t1.getTime();
            if(((a2-a)/1000 >300)){
                accountService.logout(u);
                resp.setContentType("text/html:charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            else{
                accountService.updateSession(login, t1);
                if(code.equals(accountService.getCodeForUser(login))){
                    resp.setContentType("text/html:charset=utf-8");
                    resp.setStatus(HttpServletResponse.SC_OK);
                }
                else{
                    accountService.LogUser(u);
                    resp.setContentType("text/html:charset=utf-8");
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            }
        }
    }

    public void setAccountService(AccountService accountService){
        this.accountService=accountService;
    }
}
