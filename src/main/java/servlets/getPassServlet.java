package servlets;

import com.wavesplatform.wavesj.DataEntry;
import com.wavesplatform.wavesj.Node;
import dbService.models.User;
import services.AccountService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

public class getPassServlet extends HttpServlet {

    AccountService accountService;
    Node node;

    public getPassServlet() {
    }

    public void setAccountService(AccountService accountService){
        this.accountService=accountService;
    }

    public void setNode(Node node){
        this.node=node;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login=req.getParameter("login");
        String pass=req.getParameter("password");
        String descr=req.getParameter("description");
        String address=req.getParameter("address");
        Timestamp t1= new Timestamp(System.currentTimeMillis());
        boolean b=accountService.userIsLogged(login);
        boolean m=accountService.userIsLoggedM(login);
        if(b==false || m ==false){
            resp.setContentType("text/html:charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        if(login==null ||pass==null){
            resp.setContentType("text/html:charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        User user = accountService.getUserByLogin(login);
        if(user==null || !user.getPassword().equals(pass)){
            resp.setContentType("text/html:charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        else{
            Timestamp t2 =accountService.getTimestamp(login);
            long a=t2.getTime();
            long a2=t1.getTime();
            if(((a2-a)/1000 >300)){
                accountService.logout(user);
                resp.setContentType("text/html:charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            else{
                accountService.updateSession(login, t1);
                DataEntry arr =node.getDataByKey(address,descr);
                String pas= arr.getValue().toString();
                resp.setContentType("text/html:charset=utf-8");
                resp.setHeader("pass", pas);
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }
}
