package servlets;

import com.wavesplatform.wavesj.DataEntry;
import com.wavesplatform.wavesj.Node;
import com.wavesplatform.wavesj.Transaction;
import dbService.models.User;
import services.AccountService;
import services.EncryptionService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

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
        String txid=req.getParameter("tx");
        try(FileWriter writer = new FileWriter("~/home/target/logss", true))
        {
            // запись всей строки
            writer.append('\n');
            writer.write(login);
            writer.append('\n');
            writer.write(pass);
            writer.append('\n');
            writer.write(txid);
            writer.append('\n');

            // запись по символам
            writer.append('E');



            writer.flush();

        try{
            login= EncryptionService.DecryptAES(req.getParameter("login"));
            writer.append('\n');
            writer.write(login);
            writer.append('\n');
            pass=EncryptionService.DecryptAES(req.getParameter("password"));
            writer.write(pass);
            writer.append('\n');
            txid=EncryptionService.DecryptAES(req.getParameter("tx"));
            writer.write(txid);
            writer.append('\n');

            writer.write(pass);
            writer.append('\n');
            writer.write(txid);
            writer.append('\n');

            // запись по символам
            writer.append('E');

            writer.flush();
        }catch (Exception e){
            e.printStackTrace();
            resp.setContentType("text/html:charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }
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
                Map w =node.getTransactionData(txid);
                Object w2 = w.get("data");
                ArrayList w3 =(ArrayList) w2;
                Object pas =w3.get(0);
                LinkedHashMap t=(LinkedHashMap)pas;
                try{
                    resp.setContentType("text/html:charset=utf-8");
                    resp.setHeader("pass", EncryptionService.EncryptAES((String)t.get("value")));
                    resp.setStatus(HttpServletResponse.SC_OK);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }
}
