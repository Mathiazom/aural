package com.mzom.aural.utils;

import androidx.annotation.NonNull;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TaskIterator {


    private final TimerTask timerTask;

    private final long period;

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    private ScheduledFuture<?> iteration;

    private boolean isStarted;


    public TaskIterator(@NonNull TimerTask timerTask, long period){

        this.timerTask = timerTask;

        this.period = period;

    }


    /**
     *
     * Starts iteration of progress storing tasks
     *
     */

    public void start(){

        iteration = executorService.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timerTask.run();
            }
        },0,period, TimeUnit.MILLISECONDS);

        isStarted = true;

    }

    public boolean isStarted(){

        return isStarted;

    }


    /**
     *
     * Terminates iteration of {@link TaskIterator#timerTask}
     *
     */

    public void stop(){

        if(iteration == null || iteration.isCancelled()){

            // Iteration already terminated
            return;

        }

        iteration.cancel(false);
        iteration = null;

        isStarted = false;

    }


}
