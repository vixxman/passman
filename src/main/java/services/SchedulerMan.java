package services;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class SchedulerMan {

    static Scheduler sch;

    public SchedulerMan() throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        sch = schedulerFactory.getScheduler();
        sch.start();
    }

    public static void setJob(String login){
        try{
            JobDetail jobDetail= JobBuilder.newJob(AccountService.class).withIdentity(login, "group1").usingJobData("login", login).build();
            SimpleTrigger t =(SimpleTrigger) TriggerBuilder.newTrigger().withIdentity("myTrigger", "group1").startAt(DateBuilder.futureDate(5, DateBuilder.IntervalUnit.MINUTE)).forJob(jobDetail).build();
            sch.scheduleJob(jobDetail,t);
        }
        catch (SchedulerException e){
            e.printStackTrace();
        }
    }
}
