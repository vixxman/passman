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
        String login=req.getParameter("login");
        /*try{
            login = EncryptionService.DecryptAES(login);
        }catch (Exception e){
            e.printStackTrace();
            resp.setContentType("text/html:charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }*/
        boolean b=accountService.userIsLoggedM(login);
        //boolean m=accountService.userIsLogged(login);

        if(b==false){
            resp.setContentType("text/html:charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else{
            resp.setContentType("text/html:charset=utf-8");
            String c =accountService.getCodeForUser(login);
            try{
                c=EncryptionService.EncryptAES(c);
                resp.setHeader("antichit", c);
                resp.setStatus(HttpServletResponse.SC_OK);
                return;
            }catch (Exception e){
                e.printStackTrace();
                resp.setContentType("text/html:charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                return;
            }

        }
    }

    public void setAccountService(AccountService accountService){
        this.accountService=accountService;
    }
}
