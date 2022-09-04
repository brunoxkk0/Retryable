package dev.br.brunoxkk0.retryable.core;

import java.util.concurrent.ThreadPoolExecutor;

public class RetryableTaskExecutor {

    private final int max_attempts;
    private final ThreadPoolExecutor executor;

    public RetryableTaskExecutor(int max_attempts, ThreadPoolExecutor executor){
        this.max_attempts = max_attempts;
        this.executor = executor;
    }

    public void queue(RetryableTask<?> task){

        if(task != null){

            if(task.getState().equals(TaskState.DONE) || task.getState().equals(TaskState.ERROR)){
                return;
            }

            task.updateState(TaskState.QUEUED);
            task.setRetryableTaskExecutorInstance(this);

            executor.submit(createControllableTask(task));
        }

    }

    private Runnable createControllableTask(RetryableTask<?> task){
        return () -> {
            if(task != null){

                if(task.getState().equals(TaskState.ERROR) || task.getState().equals(TaskState.DONE)){
                    return;
                }

                if(task.getAttempt() > max_attempts){
                    task.updateState(TaskState.ERROR);
                    return;
                }

                task.updateState((task.getAttempt() == 0) ? TaskState.PROCESSING : TaskState.REPROCESSING);
                task.run();
            }
        };
    }

    public int getMaxAttempts() {
        return max_attempts;
    }
}
