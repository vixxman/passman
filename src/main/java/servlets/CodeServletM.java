package servlets;

import services.AccountService;
import services.EncryptionService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CodeServletM extends HttpServlet {
    private AccountService accountService;

    public void CodeServletM(){
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login;
        try{
            login = EncryptionService.DecryptAES(req.getParameter("login"));
        }catch (Exception e){
            e.printStackTrace();
            resp.setContentType("text/html:charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }
        boolean b=accountService.userIsLoggedM(login);
        boolean m=accountService.userIsLogged(login);

        if(b==false || m==false){
            resp.setContentType("text/html:charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else{
            resp.setContentType("text/html:charset=utf-8");
            resp.setHeader("antichit", accountService.getCodeForUser(login));
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

    public void setAccountService(AccountService accountService){
        this.accountService=accountService;
    }
}
