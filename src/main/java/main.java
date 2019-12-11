import com.wavesplatform.wavesj.Account;
import com.wavesplatform.wavesj.Node;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import dbService.dbService;
import services.AccountService;
import servlets.*;



public class main {
    public static void main(String args[]) throws Exception{
        //Server server=new Server(Integer.parseInt(System.getenv("PORT")));
        Server server=new Server(8080);
        dbService dbS=new dbService();

        AccountService accountService=new AccountService(dbS);
        Node node = new Node("https://testnode1.wavesnodes.com/", Account.TESTNET);

        ServletContextHandler context=new ServletContextHandler(ServletContextHandler.SESSIONS);
        SignInServlet signInServlet=new SignInServlet();
        signInServlet.setAccountService(accountService);
        context.addServlet(new ServletHolder(signInServlet),"/signin");

        SignInM signInM=new SignInM();
        signInM.setAccountService(accountService);
        context.addServlet(new ServletHolder(signInM), "/signinm");

        SavePassServlet savePassServlet=new SavePassServlet();
        savePassServlet.setAccountService(accountService);
        savePassServlet.setNode(node);
        context.addServlet(new ServletHolder(savePassServlet), "/savePass");

        getPassServlet getPassServlet=new getPassServlet();
        getPassServlet.setAccountService(accountService);
        getPassServlet.setNode(node);
        context.addServlet(new ServletHolder(getPassServlet),"/getPass");

        Default def=new Default();
        context.addServlet(new ServletHolder(def),"/");


        SignUpServlet signUpServlet=new SignUpServlet();
        signUpServlet.setAccountService(accountService);
        context.addServlet(new ServletHolder(signUpServlet),"/signup");

        CodeServlet codeServlet=new CodeServlet();
        codeServlet.setAccountService(accountService);
        context.addServlet(new ServletHolder(codeServlet),"/code");

        CodeServletM codeServletM=new CodeServletM();
        codeServletM.setAccountService(accountService);
        context.addServlet(new ServletHolder(codeServletM),"/codem");

        SignOutM signOutM=new SignOutM();
        signOutM.setAccountService(accountService);
        context.addServlet(new ServletHolder(signOutM),"/signoutm");

        SignOutServlet signOut=new SignOutServlet();
        signOut.setAccountService(accountService);
        context.addServlet(new ServletHolder(signOut),"/signout");


        server.setHandler(context);

        server.start();
        System.out.println("Server started");
        server.join();

    }
}
