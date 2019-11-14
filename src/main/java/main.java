import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import dbService.dbService;
import services.AccountService;
import servlets.CodeServlet;
import servlets.SignInM;
import servlets.SignInServlet;
import servlets.SignUpServlet;


public class main {
    public static void main(String args[]) throws Exception{
        Server server=new Server(8080);
        dbService dbS=new dbService();
        AccountService accountService=new AccountService(dbS);

        ServletContextHandler context=new ServletContextHandler(ServletContextHandler.SESSIONS);
        SignInServlet signInServlet=new SignInServlet();
        signInServlet.setAccountService(accountService);
        context.addServlet(new ServletHolder(signInServlet),"/signin");

        SignInM signInM=new SignInM();
        signInM.setAccountService(accountService);
        context.addServlet(new ServletHolder(signInM), "/signinm");



        SignUpServlet signUpServlet=new SignUpServlet();
        signUpServlet.setAccountService(accountService);
        context.addServlet(new ServletHolder(signUpServlet),"/signup");

        CodeServlet codeServlet=new CodeServlet();
        codeServlet.setAccountService(accountService);
        context.addServlet(new ServletHolder(codeServlet),"/code");


        server.setHandler(context);

        server.start();
        System.out.println("Server started");
        server.join();

    }
}
