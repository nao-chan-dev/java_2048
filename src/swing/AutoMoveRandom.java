package swing;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AutoMoveRandom {

    private static final long MOVE_DELAY = 10;
    private static ScheduledExecutorService scheduledExecutorService;

    private static boolean autoRandom = false;

    public static void toggle(Runnable randomAction) {
        autoRandom = !autoRandom;
        if(!autoRandom) {
            scheduledExecutorService.shutdown();
            return;
        }
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(randomAction, 0, MOVE_DELAY, TimeUnit.MILLISECONDS);
    }
}
