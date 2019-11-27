package services;

import dbService.models.User;
import dbService.dbService;
import org.quartz.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;

public class AccountService implements Job{
    private final Map<String, User> loginToProfile1;
    private final Map<String, User> loginToProfileM;
    private final Map<String, String> codeForUser;
    private dbService dbS;

    public AccountService(dbService dbS){
        loginToProfile1= new HashMap();
        loginToProfileM=new HashMap();
        codeForUser=new HashMap();
        this.dbS=dbS;
    }

    public void AddNewUser(User user){
        try{
            dbS.addUser(user);
            loginToProfile1.put(user.getLogin(),user);
            setCodeForUser(user.getLogin());
        }
        catch (Exception ex){

        }
    }

    public void LogUser(User user){
        loginToProfile1.put(user.getLogin(), user);
        setCodeForUser(user.getLogin());
    }
    public void LogUserM(User user){
        loginToProfileM.put(user.getLogin(), user);
    }


    public User getUserByLogin(String login){
        return dbS.getUser(login);
    }

    public void setCodeForUser(String login){
        codeForUser.put(login,setCode(login));
    }
    public boolean userIsLogged(String user){
        return loginToProfile1.containsKey(user);
    }

    public boolean userIsLoggedM(String login){
        return loginToProfileM.containsKey(login);
    }

    public String getCodeForUser(String user){
        return codeForUser.get(user);
    }

    public void execute(JobExecutionContext context){
        JobDataMap jdm=context.getJobDetail().getJobDataMap();
        String s=jdm.getString("login");
        System.out.println(s);
        loginToProfile1.remove(s);
        codeForUser.remove(s);
        setCodeForUser(s);
    }

    public void logout(User user){
        loginToProfile1.remove(user.getLogin());
        codeForUser.remove(user.getLogin());
    }

    public void logoutM(User user){
        loginToProfileM.remove(user.getLogin());
        codeForUser.remove(user.getLogin());
    }

    private String setCode(String name){
        Random r=new Random();
        String hash=DigestUtils.sha256Hex(name);
        String code="";
        char[] arr=hash.toCharArray();
        for (int i=0;i<4;i++){
            code+=arr[r.nextInt(hash.length()-1)];
        }
        return code.toUpperCase();
    }

}
