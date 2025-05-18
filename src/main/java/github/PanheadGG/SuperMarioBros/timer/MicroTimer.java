package github.PanheadGG.SuperMarioBros.timer;

/**
 * Created by daniel on 28/10/2015.
 */
public class MicroTimer {
    public static void sleep(long micros){
        long waitUntil = System.nanoTime() + (micros * 1_000);
        while(waitUntil > System.nanoTime()){
            ;
        }
    }
}
