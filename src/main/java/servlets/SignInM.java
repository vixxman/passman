package servlets;

import com.google.common.base.Utf8;
import dbService.models.User;
import org.slf4j.Logger;
import services.AccountService;
import services.EncryptionService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.logging.LogManager;

public class SignInM extends HttpServlet {

    private AccountService accountService;

    public SignInM(){

    }

    public void setAccountService(AccountService accountService){
        this.accountService=accountService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { //авторизация
        String login=req.getParameter("login");
        String password=req.getParameter("password");
        /*try{
            login= EncryptionService.DecryptAES(login);
            System.out.println(login);

            password=EncryptionService.DecryptAES(password);
            System.out.println(password);

        }catch (Exception e){
            e.printStackTrace();
            resp.setContentType("text/html:charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }*/
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
            if(accountService.userIsLoggedM(user.getLogin())){
                resp.setContentType("text/html:charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_FOUND);
            }
            else{
                accountService.LogUserM(user);
                resp.setContentType("text/html:charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }
}
