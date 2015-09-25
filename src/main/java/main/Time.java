package main;

/**
 * Created by usr on 25.09.15.
 */
public class Time {
    public static void sleep(int period){
        try{
            Thread.sleep(period);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
