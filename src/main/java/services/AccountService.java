package services;

import dbService.models.User;
import dbService.dbService;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;

public class AccountService implements Job{
    private final Map<String, User> loginToProfile1;
    private final Map<String, User> loginToProfileM;
    private final Map<String, String> codeForUser;
    private dbService dbS;
    private Scheduler scheduler;
    private Trigger t;

    public AccountService(dbService dbS) throws SchedulerException {
        loginToProfile1= new HashMap();
        loginToProfileM=new HashMap();
        codeForUser=new HashMap();
        this.dbS=dbS;
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        this.scheduler = schedulerFactory.getScheduler();

        scheduler.start();
    }

    public void AddNewUser(User user){
        try{
            dbS.addUser(user);
            loginToProfile1.put(user.getLogin(),user);
            setCodeForUser(user.getLogin());
            setJob(user.getLogin());
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void LogUser(User user){
        loginToProfile1.put(user.getLogin(), user);
    }
    public void LogUserM(User user){
        loginToProfileM.put(user.getLogin(), user);
    }

    public void setJob(String login){
        try{
            JobDetail jobDetail= JobBuilder.newJob(AccountService.class).withIdentity(login).build();
            if(t==null) t= TriggerBuilder.newTrigger().startAt(DateBuilder.futureDate(10, DateBuilder.IntervalUnit.MINUTE)).forJob(jobDetail).build();
            scheduler.scheduleJob(jobDetail,t);
        }
        catch (SchedulerException e){
            e.printStackTrace();
        }
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
        String s=context.getJobDetail().getDescription();
        loginToProfile1.remove(s);
        codeForUser.remove(s);
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
