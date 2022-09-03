package dev.br.brunoxkk0.retryable.core;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

public class RetryableTaskExecutor {

    private static final Queue<RetryableTask<?>> taskQueue = new LinkedList<>();
    private static final AtomicBoolean atomicBooleanRunning = new AtomicBoolean(true);

    private final int MAX_ATTEMPTS = 3;

    private static final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3, r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(false);
        thread.setName("RetryableTask");
        return thread;
    });

    public void queue(RetryableTask<?> task){

        if(task != null){

            if(task.getState().equals(TaskState.DONE) || task.getState().equals(TaskState.ERROR)){
                return;
            }

            task.updateState(TaskState.QUEUED);
            task.setRetryableTaskExecutorInstance(this);

            taskQueue.add(task);
        }

    }

    public RetryableTaskExecutor(){

        executor.submit(() -> {

           while (atomicBooleanRunning.get()){

               RetryableTask<?> task = taskQueue.poll();

               if(task != null){

                   if(task.getState().equals(TaskState.ERROR) || task.getState().equals(TaskState.DONE)){
                       return;
                   }

                   if(task.getAttempt() > MAX_ATTEMPTS){
                       task.updateState(TaskState.ERROR);
                       System.out.println("Task:..." + task.getName() + " STATE: " + task.getState());
                       return;
                   }

                   task.updateState((task.getAttempt() == 0) ? TaskState.PROCESSING : TaskState.REPROCESSING);

                   System.out.println("Task:..." + task.getName() + " STATE: " + task.getState() + " TRY: " + task.getAttempt());

                   task.run();

               }
           }
        });
    }

    public void stop(){
        atomicBooleanRunning.set(false);
    }

    public int getMaxAttempts() {
        return MAX_ATTEMPTS;
    }
}
