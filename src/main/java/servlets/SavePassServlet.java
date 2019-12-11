package servlets;
import com.wavesplatform.wavesj.*;
import dbService.models.User;
import services.AccountService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SavePassServlet extends HttpServlet {

    final long FEE = 100000;
    private AccountService accountService;
    private Node node;

    public void setAccountService(AccountService accountService){
        this.accountService=accountService;
    }

    public void setNode(Node node){
        this.node=node;
    }

    public SavePassServlet() {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String seed=req.getParameter("seed");
        String login=req.getParameter("login");
        String pass=req.getParameter("password");
        String password=req.getParameter("pass");
        String description=req.getParameter("description");
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
            Timestamp t2 =user.getSession();
            long a=t2.getTime();
            long a2=t1.getTime();
            if(((a2-a)/1000 >300)){
                accountService.logout(user);
                resp.setContentType("text/html:charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            else{
                accountService.updateSession(login, t1);
                List<DataEntry> arr =node.getData("3Mqpof7XsDouQJEYoMfq9twbJEZUnFSKARb");
                for(DataEntry q : arr){
                    if(q.getKey()==description){
                        resp.setContentType("text/html:charset=utf-8");
                        resp.setStatus(HttpServletResponse.SC_FOUND);
                    }
                }
                ArrayList<DataEntry<?>> arrayList =new ArrayList<DataEntry<?>>();
                PrivateKeyAccount acc=PrivateKeyAccount.fromSeed(seed,0, Account.TESTNET);
                DataEntry stringEntry=new DataEntry.StringEntry(description, password);
                arrayList.add(stringEntry);
                String txid =node.data(acc, arrayList , FEE);
                resp.setContentType("text/html:charset=utf-8");
                resp.setHeader("tx", txid);
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }
}
